package com.github.bun133.nngraphics.util

/**
 * Do Runnable only once
 */
class Once(val r: () -> Unit) {
    var b = false
    fun once() {
        if (!b) {
            r()
            b = true
        }
    }
}

class OnceT<T>(val r: (T) -> Unit) {
    var b = false
    fun once(t: T) {
        if (!b) {
            r(t)
            b = true
        }
    }
}

class LOnce() {
    var r = {}
    var b = false
    fun once() {
        if (!b) {
            r()
            b = true
        }
    }
}

class LOnceT<T>() {
    var r = { _: T -> }
    var b = false
    fun once(t: T) {
        if (!b) {
            r(t)
            b = true
        }
    }
}

class SOnce(private val id: String, val r: () -> Unit) {
    companion object{
        val map = mutableMapOf<String, Boolean>()
    }

    fun once() {
        if (!map.containsKey(id) || !map[id]!!){
            r()
            map[id] = true
        }
    }
}