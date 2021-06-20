package com.github.bun133.nngraphics

import com.github.bun133.nngraphics.display.*
import java.awt.event.MouseEvent

fun main() {
    val d = JFrameDisplay(bound = Rect(100, 100, 500, 500))
    /*
    val scene = d.scene()
    scene.newLayer().add(object : Drawable {
        override fun onDraw(g: NGraphic) {
            g.setColor(Color.BLACK)
            g.rect(Rect(100, 100, 200, 200))
        }

        override fun pos(): Pos = Pos(0, 0)
        override fun setPos(pos: Pos) {}
    })*/

    d.mouse.listeners.add(object : MouseBoundedListener<MouseEvent> {
        override fun bound(): Rect = Rect(100, 100, 500, 500)
        override fun type(): List<Mouse.Type>? = null
        override fun on(p: Pos, t: Mouse.Type, event: MouseEvent) {
            println("[Mouse]Pos:${p.x},${p.y} Type:${t.name},e:$event")
        }

        override var isIn: Boolean = false
    })

    while (true) {
        d.draw.update()
    }
}