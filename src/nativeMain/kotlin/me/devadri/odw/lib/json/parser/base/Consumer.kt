package me.devadri.odw.lib.json.parser.base

typealias Consumer = Parser<Unit>

fun Parser<*>.consume(): Consumer = map { }
