package me.devadri.odw.lib.json.ast

data class JsonInteger(val string: String) : JsonPrimitive {
    val int: Int get() = string.toInt()
}
