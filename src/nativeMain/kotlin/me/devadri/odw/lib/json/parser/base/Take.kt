package me.devadri.odw.lib.json.parser.base

import me.devadri.odw.lib.json.parser.dsl.ParserState
import me.devadri.odw.lib.json.parser.dsl.discard

fun ParserState.takeExact(n: Int): String {
    val string = source.take(n)
    if (string.length != n) fail()
    discard(n)
    return string
}
