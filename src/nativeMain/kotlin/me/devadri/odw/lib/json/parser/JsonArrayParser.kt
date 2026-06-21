package me.devadri.odw.lib.json.parser

import me.devadri.odw.lib.json.ast.JsonArray
import me.devadri.odw.lib.json.parser.base.*
import me.devadri.odw.lib.json.parser.dsl.parser

fun jsonArrayParser(): Parser<JsonArray> = parser {
    char('[')
    whitespace()
    val nodes = manySeparated(
        elementParser = jsonNodeParser(failOnRemaining = false),
        separatorConsumer = commaConsumer()
    )
    whitespace()
    char(']')
    JsonArray(nodes)
}
