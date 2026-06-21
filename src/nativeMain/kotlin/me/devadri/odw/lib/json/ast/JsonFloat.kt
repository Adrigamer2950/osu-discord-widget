package me.devadri.odw.lib.json.ast

data class JsonFloat(val string: String) : JsonPrimitive {
    val float: Float get() = string.toFloat()
}
