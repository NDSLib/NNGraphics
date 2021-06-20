package com.github.bun133.nngraphics.display

import java.awt.Color
import java.awt.Graphics

interface NGraphic {
    fun setRGB(pos: Pos, rgb: Color)

    fun getRGB(pos: Pos): Color

    fun setColor(c: Color)
    fun getColor(): Color

    fun line(from: Pos, to: Pos)
    fun rect(left_up: Pos, right_down: Pos, isFill: Boolean = true) {
        rect(Rect(left_up, right_down), isFill)
    }

    fun rect(rect: Rect, isFill: Boolean = true)
}

class GraphicsWrapper(val graphics: Graphics) : NGraphic {
    companion object {
        fun convert(g: NGraphic): GraphicsWrapper? {
            return if (g !is GraphicsWrapper) {
                null
            } else {
                g
            }
        }

        fun graphics(g:NGraphic):Graphics? {
            return convert(g)?.graphics
        }
    }

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
        graphics.drawLine(from.x, from.y, to.x, to.y)
    }

    override fun rect(rect: Rect, isFill: Boolean) {
        if (isFill) {
            graphics.fillRect(rect.left_up.x, rect.left_up.x, rect.width(), rect.height())
        } else {
            graphics.drawRect(rect.left_up.x, rect.left_up.x, rect.width(), rect.height())
        }
    }
}

interface Drawable {
    fun onDraw(g: NGraphic)
    // 特に何かを縛りはしないけど左上の座標
    fun pos():Pos
    fun setPos(pos:Pos)
}

abstract class GraphicsDrawable:Drawable{
    override fun onDraw(g: NGraphic) {
        val gg = GraphicsWrapper.graphics(g)
        if (gg != null) {
            onDraw(gg)
        }
    }
    abstract fun onDraw(gg:Graphics)
}