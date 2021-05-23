package com.github.bun133.nngraphics.display

import java.awt.Graphics2D
import java.awt.Toolkit
import java.awt.event.*
import java.awt.image.BufferStrategy
import javax.swing.JFrame

class JFrameDisplay(
    name: String = "NNGraphics",
    bound: Rect,
    closeOperation: Int = JFrame.EXIT_ON_CLOSE,
    bufferSize: Int = 3
) : Display() {
    override val mouse: JFrameMouseWrapper = JFrameMouseWrapper(this)
    override val draw: JFrameDrawManger = JFrameDrawManger(60.0, this)
    override val windowListener: JFrameWindowListener = JFrameWindowListener(this)
    lateinit var jframe: JFrame
    lateinit var buffer: BufferStrategy

    init {
        eventHandler.register {
            when (it) {
                Events.DisplayInitStart -> {
                    jframe = JFrame(name)
                    jframe.setBounds(bound.left_up.x, bound.left_up.y, bound.width(), bound.height())
                    jframe.defaultCloseOperation = closeOperation
                    jframe.isVisible = true
                    jframe.createBufferStrategy(bufferSize)
                    buffer = jframe.bufferStrategy
                }
                else -> {
                }
            }
        }

        eventHandler.execute(Events.EventListenerAllReady)
    }

    override fun getGraphics(): NGraphic? {
        if (buffer.contentsLost()) {
            buffer = jframe.bufferStrategy
        }
        val g = buffer.drawGraphics
        return if (g == null) {
            null
        } else {
            GraphicsWrapper(g)
        }
    }
}

class JFrameMouseWrapper(val jframe: JFrameDisplay) : Mouse<MouseEvent>(), MouseListener, MouseMotionListener {
    init {
        jframe.eventHandler.register {
            @Suppress("NON_EXHAUSTIVE_WHEN")
            when (it) {
                Display.Events.DisplayInitEnd -> {
                    jframe.jframe.addMouseListener(this)
                    jframe.jframe.addMouseMotionListener(this)
                }
            }
        }
    }

    var pos = Pos(0, 0)
    override fun getNowPos(): Pos = pos
    override fun getType(event: MouseEvent): Type? {
        if (event.button != 0) {
            return when (event.button) {
                MouseEvent.BUTTON1 -> {
                    Type.LeftClick
                }
                MouseEvent.BUTTON2 -> {
                    Type.RightClick
                }
                else -> {
                    Type.OtherClick
                }
            }
        }

        when (event.id) {
            MouseEvent.MOUSE_DRAGGED -> {
                return Type.Drag
            }
            MouseEvent.MOUSE_MOVED -> {
                return Type.Move
            }
            MouseEvent.MOUSE_EXITED -> {
                return Type.Exit
            }
            MouseEvent.MOUSE_ENTERED -> {
                return Type.Enter
            }
        }
        return null
    }

    private fun updatePos(e: MouseEvent) {
        pos = Pos(e.point)
    }


    ////////// Listeners ////////////
    override fun mouseClicked(e: MouseEvent) {
        updatePos(e)
        invoke(e)
    }

    override fun mousePressed(e: MouseEvent) {
        updatePos(e)
        invoke(e)
    }

    override fun mouseReleased(e: MouseEvent) {
        updatePos(e)
        invoke(e)
    }

    override fun mouseEntered(e: MouseEvent) {
        updatePos(e)
        invoke(e)
    }

    override fun mouseExited(e: MouseEvent) {
        updatePos(e)
        invoke(e)
    }

    override fun mouseDragged(e: MouseEvent) {
        updatePos(e)
        invoke(e)
    }

    override fun mouseMoved(e: MouseEvent) {
        updatePos(e)
        invoke(e)
    }
}

class JFrameWindowListener(val display: JFrameDisplay) : WinListener<WindowEvent>(), WindowListener {
    init {
        display.eventHandler.register {
            @Suppress("NON_EXHAUSTIVE_WHEN")
            when (it) {
                Display.Events.DisplayInitEnd -> {
                    display.jframe.addWindowListener(this)
                }
            }
        }
    }


    //////// Listener ////////
    override fun windowOpened(e: WindowEvent) {
        on(Type.Open, e)
    }

    override fun windowClosing(e: WindowEvent) {
        on(Type.Closing, e)
    }

    override fun windowClosed(e: WindowEvent) {
        on(Type.Closed, e)
    }

    override fun windowIconified(e: WindowEvent) {
        on(Type.Minimised, e)
    }

    override fun windowDeiconified(e: WindowEvent) {
        on(Type.UnMinimised, e)
    }

    override fun windowActivated(e: WindowEvent) {
        on(Type.Active, e)
    }

    override fun windowDeactivated(e: WindowEvent) {
        on(Type.UnActive, e)
    }

}

class JFrameDrawManger(fps: Double, override val display: JFrameDisplay): DrawManager(fps, display) {
    override fun update(){
        super.update()
        Toolkit.getDefaultToolkit().sync()
        if (!display.buffer.contentsLost()) display.buffer.show()
    }
}