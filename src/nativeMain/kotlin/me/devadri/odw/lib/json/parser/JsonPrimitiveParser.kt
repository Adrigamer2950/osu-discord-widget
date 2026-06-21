package me.devadri.odw.lib.json.parser

import me.devadri.odw.lib.json.ast.JsonPrimitive
import me.devadri.odw.lib.json.parser.base.Parser
import me.devadri.odw.lib.json.parser.base.anyParser

fun jsonPrimitiveParser(): Parser<JsonPrimitive> = anyParser(
    jsonNullParser(),
    jsonBooleanParser(),
    jsonFloatParser(),
    jsonIntegerParser(),
    jsonStringParser()
)
