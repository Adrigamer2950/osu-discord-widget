package me.devadri.odw.lib.json.parser.base

import me.devadri.odw.lib.json.parser.dsl.ParserState

fun <T> ParserState.many(elementParser: Parser<T>): List<T> {
    val results: MutableList<T> = mutableListOf()

    while (true) {
        val result = elementParser
            .tryParse()
            .getOrElse { return results }

        results += result
    }
}
