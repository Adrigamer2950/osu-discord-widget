package me.devadri.odw.lib.json.parser

import me.devadri.odw.lib.json.ast.JsonNode
import me.devadri.odw.lib.json.ast.JsonObject
import me.devadri.odw.lib.json.parser.base.*
import me.devadri.odw.lib.json.parser.dsl.parser

fun jsonObjectParser(): Parser<JsonObject> = parser {
    char('{')
    whitespace()
    val pairs = manySeparated(
        elementParser = pairParser(),
        separatorConsumer = commaConsumer()
    )
    whitespace()
    char('}')

    JsonObject(pairs.toMap())
}

private fun pairParser(): Parser<Pair<String, JsonNode>> = parser {
    val key = jsonString()
    whitespace()
    char(':')
    whitespace()
    val node = jsonNode()
    key.string to node
}
