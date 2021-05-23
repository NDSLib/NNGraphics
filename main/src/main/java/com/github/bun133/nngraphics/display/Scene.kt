package com.github.bun133.nngraphics.display

class Scene {
    var layers = mutableListOf<Layer>()
    fun newLayer():Layer{
        val l = Layer()
        layers.add(l)
        return l
    }
}

class Layer{
    var drawables = mutableListOf<Drawable>()
    fun add(d:Drawable){drawables.add(d)}
}