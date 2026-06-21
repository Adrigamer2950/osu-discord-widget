package me.devadri.odw.lib.json

import me.devadri.odw.lib.json.ast.JsonBoolean
import me.devadri.odw.lib.json.ast.JsonFloat
import me.devadri.odw.lib.json.ast.JsonInteger
import me.devadri.odw.lib.json.ast.JsonObject
import me.devadri.odw.lib.json.ast.JsonPrimitive
import me.devadri.odw.lib.json.ast.JsonString

fun JsonObject.getPrimitive(path: String): JsonPrimitive? {
    var jsonObj: JsonObject = this

    val subPaths = path.split(".")

    for ((index, subPath) in subPaths.withIndex()) {
        val isLast = index == subPaths.lastIndex

        if (isLast) {
            return (jsonObj.map[subPath] as? JsonPrimitive)
        } else {
            jsonObj = jsonObj.map[subPath] as? JsonObject
                ?: throw NullPointerException("Tried to access null json object '$subPath' in $path")
        }
    }

    return null
}

fun JsonObject.getString(path: String): String? = (this.getPrimitive(path) as? JsonString)?.string

fun JsonObject.getFloat(path: String): Float? = (this.getPrimitive(path) as? JsonFloat)?.float

fun JsonObject.getInteger(path: String): Int? = (this.getPrimitive(path) as? JsonInteger)?.int

fun JsonObject.getBoolean(path: String): Boolean? = (this.getPrimitive(path) as? JsonBoolean)?.boolean