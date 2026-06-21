package me.devadri.odw.lib.json.parser.base

import me.devadri.odw.lib.json.parser.dsl.ParserState
import me.devadri.odw.lib.json.parser.dsl.discard
import me.devadri.odw.lib.json.parser.dsl.parser

fun stringParser(string: String): Parser<String> = parser { string(string) }

fun stringConsumer(string: String): Consumer = stringParser(string).consume()

fun ParserState.string(string: String): String {
    if (!source.startsWith(string)) fail()
    discard(string.length)
    return string
}
