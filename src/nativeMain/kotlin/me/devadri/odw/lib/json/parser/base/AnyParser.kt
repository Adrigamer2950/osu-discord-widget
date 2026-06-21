package me.devadri.odw.lib.json.parser.base

import me.devadri.odw.lib.json.parser.dsl.ParserState
import me.devadri.odw.lib.json.parser.dsl.parser

fun <T> anyParser(vararg parsers: Parser<T>): Parser<T> = anyParser(parsers.toList())

fun <T> anyParser(list: List<Parser<T>>): Parser<T> = parser { any(list) }

fun <T> ParserState.any(vararg parsers: Parser<T>): T = any(parsers.toList())

fun <T> ParserState.any(list: List<Parser<T>>): T {
    for (parser in list) {
        parser
            .tryParse()
            .onSuccess { value -> return value }
    }
    fail()
}
