package me.devadri.odw.lib.json.parser.dsl

fun ParserState.discard(n: Int) {
    try {
        source = source.substring(n)
    } catch (_: IndexOutOfBoundsException) {
        fail()
    }
}
