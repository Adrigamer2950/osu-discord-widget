package me.devadri.odw.lib.json.parser

import me.devadri.odw.lib.json.ast.JsonNull
import me.devadri.odw.lib.json.parser.base.Parser
import me.devadri.odw.lib.json.parser.base.map
import me.devadri.odw.lib.json.parser.base.stringConsumer

fun jsonNullParser(): Parser<JsonNull> = stringConsumer(string = "null").map { JsonNull }
