@file:OptIn(ExperimentalForeignApi::class)

package me.devadri.odw.http

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.LongVar
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alignOf
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import libcurl.*
import platform.posix.size_t

object Http {
    fun send(req: HttpRequest): HttpResponse = memScoped {
        val curl = curl_easy_init() ?: error("libcurl initialization failed")

        curl_easy_setopt(curl, CURLOPT_URL, req.url)

        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 1L)
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 2L)
        curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L)

        var cHeaders: CPointer<curl_slist>? = null
        req.headers.forEach { (k, v) ->
            cHeaders = curl_slist_append(cHeaders, "$k: $v")
        }
        if (cHeaders != null) {
            curl_easy_setopt(curl, CURLOPT_HTTPHEADER, cHeaders)
        }

        val cookiesStr = req.cookies.entries.joinToString("; ") {
            (k, v) -> "$k=$v"
        }
        if (cookiesStr.isNotEmpty()) {
            curl_easy_setopt(curl, CURLOPT_COOKIE, cookiesStr)
        }

        when (req.method) {
            HttpRequest.Method.PATCH, HttpRequest.Method.POST -> {
                curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, req.method.name)
                req.body?.let {
                    curl_easy_setopt(curl, CURLOPT_COPYPOSTFIELDS, it)
                }
            }
            else -> {}
        }

        val response = StringBuilder()
        val stableRef = StableRef.create(response)

        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION,
            staticCFunction { ptr: CPointer<ByteVar>?, size: size_t, nmemb: size_t, userdata: COpaquePointer? ->
                val sb = userdata!!.asStableRef<StringBuilder>().get()
                val bytes = (size * nmemb).toInt()
                val byteArray = ptr!!.readBytes(bytes)
                sb.append(byteArray.toKString())
                bytes.convert<size_t>()
            }
        )
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, stableRef.asCPointer())

        val res = curl_easy_perform(curl)
        if (res != CURLE_OK)
            error("curl error: ${curl_easy_strerror(res)?.toKString()}")

        val rawPtr = alloc(sizeOf<LongVar>(), alignOf<LongVar>()).reinterpret<LongVar>()
        curl_easy_getinfo(curl, CURLINFO_RESPONSE_CODE, rawPtr.ptr)
        curl_slist_free_all(cHeaders)
        curl_easy_cleanup(curl)
        stableRef.dispose()
        return HttpResponse(rawPtr.value, response.toString())
    }
}