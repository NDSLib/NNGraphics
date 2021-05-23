package com.github.bun133.nngraphics.display

class Register<E> {
    private var list = mutableListOf<(E) -> Unit>()
    fun register(f: (E) -> Unit) {
        list.add(f)
    }

    fun execute(e: E) {
        list.forEach { it(e) }
    }
}

class TypedRegister<E, T> {
    private var map = mutableMapOf<T, MutableList<(E) -> Unit>>()
    fun register(f: (E) -> Unit, type: T) {
        if (!map.containsKey(type)) map[type] = mutableListOf()
        map[type]!!.add(f)
    }

    fun execute(event: E, type: T) {
        map[type]?.forEach { it(event) }
    }
}