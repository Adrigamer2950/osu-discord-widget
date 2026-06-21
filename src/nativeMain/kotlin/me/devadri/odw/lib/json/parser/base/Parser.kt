package me.devadri.odw.lib.json.parser.base

fun interface Parser<out T> {
    fun parse(source: String): ParserResult<T>
}
