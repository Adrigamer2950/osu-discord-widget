package me.devadri.odw.http

class HttpRequest(
    val url: String,
    val method: Method = Method.GET,
    val headers: Map<String, String> = emptyMap(),
    val cookies: Map<String, String> = emptyMap(),
    val body: String? = null
) {

    enum class Method {

        GET,
        POST,
        PATCH,
    }
}