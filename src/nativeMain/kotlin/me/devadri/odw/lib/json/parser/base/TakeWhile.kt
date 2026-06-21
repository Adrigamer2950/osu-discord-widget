package me.devadri.odw.lib.json.parser.base

import me.devadri.odw.lib.json.parser.dsl.ParserState
import me.devadri.odw.lib.json.parser.dsl.discard

inline fun ParserState.takeWhile(
    predicate: (Char) -> Boolean
): String {
    val result = source.takeWhile(predicate)
    discard(result.length)
    return result
}
