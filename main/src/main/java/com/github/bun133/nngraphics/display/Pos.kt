package com.github.bun133.nngraphics.display

import java.awt.Point
import java.awt.Rectangle
import java.awt.geom.Rectangle2D
import kotlin.math.roundToInt

class Pos(var x: Int, var y: Int) {
    constructor(point: Point) : this(point.x, point.y)
}

class Rect(var left_up: Pos, var right_down: Pos) {
    constructor(left_x: Int, left_y: Int, right_x: Int, right_y: Int) : this(Pos(left_x, left_y), Pos(right_x, right_y))
    constructor(r: Rectangle) : this(Pos(r.x, r.y), Pos(r.x + r.width, r.y + r.height))
    constructor(r: Rectangle2D) : this(
        r.minX.roundToInt(),
        r.minY.roundToInt(),
        r.maxX.roundToInt(),
        r.maxY.roundToInt()
    )

    fun width() = right_down.x - left_up.x
    fun height() = right_down.y - left_up.y

    fun contain(pos: Pos): Boolean {
        return left_up.x <= pos.x && pos.x <= right_down.x && left_up.y <= pos.y && pos.y <= right_down.y
    }

    fun center() = Pos(left_up.x + width() / 2, left_up.y + height() / 2)
}