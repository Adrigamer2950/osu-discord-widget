package me.devadri.odw.lib.json.parser

import me.devadri.odw.lib.json.ast.JsonBoolean
import me.devadri.odw.lib.json.ast.JsonFalse
import me.devadri.odw.lib.json.ast.JsonTrue
import me.devadri.odw.lib.json.parser.base.anyParser
import me.devadri.odw.lib.json.parser.base.*

fun jsonBooleanParser(): Parser<JsonBoolean> = anyParser(jsonTrueParser(), jsonFalseParser())

fun jsonTrueParser(): Parser<JsonTrue> = stringConsumer("true").map { JsonTrue }
fun jsonFalseParser(): Parser<JsonFalse> = stringConsumer("false").map { JsonFalse }
