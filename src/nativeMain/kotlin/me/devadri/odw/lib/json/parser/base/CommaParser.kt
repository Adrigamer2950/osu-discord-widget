package me.devadri.odw.lib.json.parser.base

import me.devadri.odw.lib.json.parser.dsl.ParserState
import me.devadri.odw.lib.json.parser.dsl.parser

fun commaConsumer(): Consumer = parser { comma() }

fun ParserState.comma() {
    whitespace()
    char(',')
    whitespace()
}
