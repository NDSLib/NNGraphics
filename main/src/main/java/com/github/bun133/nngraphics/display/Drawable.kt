package com.github.bun133.nngraphics.display

import java.awt.Color
import java.awt.Graphics

interface NGraphic {
    fun setRGB(pos: Pos, rgb: Color)

    fun getRGB(pos: Pos): Color

    fun setColor(c: Color)
    fun getColor(): Color

    fun line(from: Pos, to: Pos)
    fun rect(left_up: Pos, right_down: Pos, isFill: Boolean = true){
        rect(Rect(left_up,right_down),isFill)
    }
    fun rect(rect:Rect, isFill: Boolean = true)
}

class GraphicsWrapper(val graphics: Graphics) : NGraphic {
    override fun setRGB(pos: Pos, rgb: Color) {
        setColor(rgb)
        line(pos, pos)
    }

    override fun getRGB(pos: Pos): Color {
        throw Exception("GraphicsWrapper#getRGB NotSupported!")
    }

    override fun setColor(c: Color) {
        graphics.color = c
    }

    override fun getColor(): Color = graphics.color

    override fun line(from: Pos, to: Pos) {
        graphics.drawLine(from.x,from.y,to.x,to.y)
    }

    override fun rect(rect: Rect, isFill: Boolean) {
        if(isFill){
            graphics.fillRect(rect.left_up.x,rect.left_up.x,rect.width(),rect.height())
        }else{
            graphics.drawRect(rect.left_up.x,rect.left_up.x,rect.width(),rect.height())
        }
    }
}

interface Drawable {
    fun onDraw(g:NGraphic)
}