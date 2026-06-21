package me.devadri.odw.lib.json.parser.base

import me.devadri.odw.lib.json.parser.dsl.ParserState
import me.devadri.odw.lib.json.parser.dsl.parser

fun <T> ParserState.manySeparated(
    elementParser: Parser<T>,
    separatorConsumer: Consumer
): List<T> {
    val first = elementParser.tryParse().getOrElse { return emptyList() }

    val elementWithSeparator = parser {
        separatorConsumer.parse()
        elementParser.parse()
    }

    return listOf(first) + many(elementWithSeparator)
}
