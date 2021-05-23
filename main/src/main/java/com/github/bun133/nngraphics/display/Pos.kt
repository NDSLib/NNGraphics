package com.github.bun133.nngraphics.display

import java.awt.Point

class Pos(var x: Int, var y: Int) {
    constructor(point: Point) : this(point.x, point.y)
}

class Rect(var left_up: Pos, var right_down: Pos) {
    constructor(left_x: Int, left_y: Int, right_x: Int, right_y: Int) : this(Pos(left_x, left_y), Pos(right_x, right_y))

    init {
        if (right_down.x < left_up.x || left_up.y > right_down.y) {
            throw Exception("Illegal Rect Pos left_up.x:${left_up.x} left_up.y:${left_up.y} right_down.x:${right_down.x} right_down.y:${right_down.y}l")
        }
    }

    fun width() = right_down.x - left_up.x
    fun height() = right_down.y - left_up.y
}