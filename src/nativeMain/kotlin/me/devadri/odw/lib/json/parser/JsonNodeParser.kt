package me.devadri.odw.lib.json.parser

import me.devadri.odw.lib.json.ast.JsonNode
import me.devadri.odw.lib.json.parser.base.Parser
import me.devadri.odw.lib.json.parser.base.any
import me.devadri.odw.lib.json.parser.dsl.ParserState
import me.devadri.odw.lib.json.parser.dsl.parser

fun jsonNodeParser(failOnRemaining: Boolean = true): Parser<JsonNode> = parser {
    val node = jsonNode()
    if (failOnRemaining && source.isNotEmpty()) fail()
    node
}

fun ParserState.jsonNode(): JsonNode = any(
    jsonPrimitiveParser(),
    jsonArrayParser(),
    jsonObjectParser()
)
