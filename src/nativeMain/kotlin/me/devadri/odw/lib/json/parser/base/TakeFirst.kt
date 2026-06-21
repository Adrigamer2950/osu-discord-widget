package me.devadri.odw.lib.json.parser.base

import me.devadri.odw.lib.json.parser.dsl.ParserState
import me.devadri.odw.lib.json.parser.dsl.discard

inline fun ParserState.takeFirst(
    predicate: (Char) -> Boolean
): String {
    val result = source.firstOrNull() ?: return ""
    if (predicate(result)) {
        discard(1)
        return result.toString()
    }
    return ""
}
