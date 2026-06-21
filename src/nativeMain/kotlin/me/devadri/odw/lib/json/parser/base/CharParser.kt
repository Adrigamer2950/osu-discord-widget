package me.devadri.odw.lib.json.parser.base

import me.devadri.odw.lib.json.parser.dsl.ParserState
import me.devadri.odw.lib.json.parser.dsl.discard
import me.devadri.odw.lib.json.parser.dsl.parser

fun charParser(char: Char): Parser<Char> = parser { char(char) }

fun ParserState.char(char: Char): Char {
    if (!source.startsWith(char)) fail()
    discard(n = 1)
    return char
}

fun Parser<Char>.string() = map { it.toString() }
