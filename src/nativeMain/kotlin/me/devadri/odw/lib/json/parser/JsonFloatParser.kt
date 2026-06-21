package me.devadri.odw.lib.json.parser

import me.devadri.odw.lib.json.ast.JsonFloat
import me.devadri.odw.lib.json.parser.base.Parser
import me.devadri.odw.lib.json.parser.base.takeFirst
import me.devadri.odw.lib.json.parser.base.takeWhile
import me.devadri.odw.lib.json.parser.dsl.parser

fun jsonFloatParser(): Parser<JsonFloat> = parser {
    if (source.isEmpty()) fail()

    val minus = takeFirst { it == '-' }
    val number = takeWhile(Char::isDigit)

    if (number.isEmpty()) fail()

    val point = takeFirst { it == '.' }
    val fraction = takeWhile(Char::isDigit)

    if (point.isEmpty()) fail()

    val exponent = takeFirst { it == 'E' || it == 'e' }
    val sign = takeFirst { it == '+' || it == '-' }
    val exponentValue = takeWhile(Char::isDigit)

    if (exponent.isEmpty() && (sign.isNotEmpty() || exponentValue.isNotEmpty())) fail()
    if (exponent.isNotEmpty() && exponentValue.isEmpty()) fail()

    val result = buildString {
        append(minus)
        append(number)
        append(point)
        append(fraction)
        append(exponent)
        append(sign)
        append(exponentValue)
    }

    if (result.isEmpty()) fail()

    JsonFloat(string = result)
}
