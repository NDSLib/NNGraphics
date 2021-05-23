package com.github.bun133.nngraphics

import com.github.bun133.nngraphics.display.*
import java.awt.Color

fun main() {
    val d = JFrameDisplay(bound = Rect(100, 100, 500, 500))
    val scene = d.scene()
    scene.newLayer().add(object : Drawable {
        override fun onDraw(g: NGraphic) {
            g.setColor(Color.BLACK)
            g.rect(Rect(100,100,200,200))
        }
    })
    while(true){
        d.draw.update()
    }
}