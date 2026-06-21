package me.devadri.odw.lib.json.parser.base

import me.devadri.odw.lib.json.parser.dsl.ParserState

fun ParserState.whitespace() {
    takeWhile { it.isWhitespace() }
}
