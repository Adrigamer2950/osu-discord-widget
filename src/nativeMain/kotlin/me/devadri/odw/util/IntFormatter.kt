package me.devadri.odw.util

fun Int.toLocaleString(): String {
    val negative = this < 0
    val s = kotlin.math.abs(this).toString()
    val sb = StringBuilder()
    for ((i, c) in s.reversed().withIndex()) {
        if (i != 0 && i % 3 == 0) sb.append(',')
        sb.append(c)
    }
    return (if (negative) "-" else "") + sb.reverse().toString()
}