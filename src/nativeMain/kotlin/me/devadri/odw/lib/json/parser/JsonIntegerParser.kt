package me.devadri.odw.lib.json.parser

import me.devadri.odw.lib.json.ast.JsonInteger
import me.devadri.odw.lib.json.parser.base.Parser
import me.devadri.odw.lib.json.parser.base.takeFirst
import me.devadri.odw.lib.json.parser.base.takeWhile
import me.devadri.odw.lib.json.parser.dsl.parser

fun jsonIntegerParser(): Parser<JsonInteger> = parser {
    val minus = takeFirst { it == '-' }
    val string = takeWhile(Char::isDigit)

    if (string.isEmpty()) fail()

    JsonInteger(string = "$minus$string")
}
