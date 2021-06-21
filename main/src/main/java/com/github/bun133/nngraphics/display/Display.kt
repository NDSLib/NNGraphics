package com.github.bun133.nngraphics.display

import kotlin.math.roundToLong


abstract class Display {
    enum class Events {
        // Hooks when All EventListener is ready
        // NOTE THIS IS NEED TO BE CALLED!!!!!!
        EventListenerAllReady,

        // Hooks when Init
        DisplayInitStart,

        // Hooks when Init is done,and the other components will init
        DisplayInitEnd
    }

    val scene: SceneManager = SceneManager()
    abstract val mouse: Mouse<*>
    abstract val draw: DrawManager
    abstract val windowListener: WinListener<*>
    val eventHandler: Register<Events> = Register()
    abstract fun getGraphics(): NGraphic?

    init {
        eventHandler.register {
            when (it) {
                Events.EventListenerAllReady -> {
                    eventHandler.execute(Events.DisplayInitStart)
                    eventHandler.execute(Events.DisplayInitEnd)
                }
            }
        }
    }

    fun scene() = scene.getCurrentScene()
}

abstract class Mouse<T> {
    enum class Type {
        // 左クリック
        LeftClick,
        // 右クリック
        RightClick,
        // ホイールクリック
        WheelClick,
        // その他クリック
        OtherClick,
        // ドラッグ
        Drag,
        // マウスが進入
        Enter,
        // マウスが出で行った
        Exit,
        // マウス移動
        Move,
        // マウスリリース
        Release
    }

    abstract fun getNowPos(): Pos
    abstract fun getPosFromT(t: T): Pos?
    fun getPosX(): Int {
        return getNowPos().x
    }

    fun getPosY(): Int {
        return getNowPos().y
    }

    val register: TypedRegister<T, Type> = TypedRegister()

    fun invoke(event: T) {
        val t = getType(event) ?: return
        register.execute(event, t)
        val p = getPosFromT(event)
        if (p == null) {
            println("[NNGraphics][Warn]In Mouse,getPosFromT is Null")
            return
        }

        // RectでもEnter、Exitが欲しい。
        listeners.forEach {
            val b = it.bound().contain(p)
            if (it.isIn != b) {
                if (it.isIn) {
                    it.onInBound(p, Type.Exit, event)
                } else {
                    it.onInBound(p, Type.Enter, event)
                }
            }
            it.isIn = b
        }

        // InBound
        listeners
            .filter { it.type() == null || it.type()!!.contains(t) }
            .filter { it.bound().contain(p) }
            .forEach { it.onInBound(p, t, event) }

        // OutBound
        listeners
            .filter { it.type() == null || it.type()!!.contains(t) }
            .filter { !it.bound().contain(p) }
            .forEach { it.onOutBound(p, t, event) }
    }

    abstract fun getType(event: T): Type?

    var listeners = mutableListOf<MouseBoundedListener<T>>()
}

interface MouseBoundedListener<T> {
    fun bound(): Rect
    fun type(): List<Mouse.Type>?
    fun onInBound(p: Pos, t: Mouse.Type, event: T)
    fun onOutBound(p: Pos, t: Mouse.Type, event: T)

    // 範囲内にMouseがあるか
    var isIn: Boolean
}

class SceneManager {
    var scenes = mutableListOf<Scene>()
    var index = 0
    fun getCurrentScene(): Scene {
        val s = scenes.getOrNull(index)
        if (s != null) {
            return s
        }
        return newScene()
    }

    fun newScene(): Scene {
        val s = Scene()
        scenes.add(s)
        return s
    }

    fun setScene(index: Int) {
        this.index = index
    }
}

open class DrawManager(var fps: Double, open val display: Display, val skipMode: SkipMode = SkipMode.OverLoad) {
    enum class SkipMode {
        // スキップしても次フレームまでの描画を待つ
        OverLoad,

        // 待たない
        Quick,

        // NGraphicがnullじゃない次のタイミングで描画する
        NonNull
    }

    var lastTime: Long = 0L
    var waitingNotnull = false

    /**
     * MainTick
     */
    open fun update() {
        if (waitingNotnull) {
            if (display.getGraphics() != null) {
                calcTime()
            }
        }

        if ((System.nanoTime() - lastTime) > getFrameNs()) {
            calcTime()
        }
    }

    private fun calcTime() {
        if (draw()) {
//            println("Delta:${System.nanoTime() - lastTime}")
            lastTime = System.nanoTime()
        } else {
            println("[NNGraphics]This Frame is Skipped since NGraphic is Null")
            when (skipMode) {
                SkipMode.Quick -> {
                    //Do nothing,continue to draw
                }
                SkipMode.OverLoad -> {
                    lastTime = System.nanoTime()
                }
                SkipMode.NonNull -> {
                    waitingNotnull = true
                }
            }
        }
    }

    open fun draw(): Boolean {
        val d = display.getGraphics() ?: return false
        display.scene.getCurrentScene().layers.forEach { layer ->
            layer.drawables.forEach {
                it.onDraw(d)
            }
        }
        return true
    }

    private fun getFrameNs(): Long {
        return (1000L * 1000L * 1000L / fps).roundToLong()
    }
}

abstract class WinListener<E> {
    enum class Type {
        Closing, Closed, Open, Minimised, UnMinimised, Maximized, UnMaximized, Active, UnActive
    }

    val registry = TypedRegister<E, Type>()

    fun on(type: Type, event: E) {
        registry.execute(event, type)
    }
}