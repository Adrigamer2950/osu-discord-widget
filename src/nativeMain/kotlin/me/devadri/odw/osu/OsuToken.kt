package me.devadri.odw.osu

import me.devadri.odw.Constants
import me.devadri.odw.http.Http
import me.devadri.odw.http.HttpRequest
import me.devadri.odw.lib.json.ast.JsonObject
import me.devadri.odw.lib.json.ast.JsonString
import me.devadri.odw.lib.json.parser.base.Parser
import me.devadri.odw.lib.json.parser.base.ParserResult
import me.devadri.odw.lib.json.parser.jsonObjectParser

object OsuToken {

    fun getToken(): String = try {
        val json = """
{ 
"client_id": "${Constants.OSU_CLIENT_ID}", 
"client_secret": "${Constants.OSU_CLIENT_SECRET}", 
"grant_type": "client_credentials",  
"scope": "public" 
}
""".trimIndent().replace("\n", "")

        val res = Http.send(
            HttpRequest(
                url = "https://osu.ppy.sh/oauth/token",
                method = HttpRequest.Method.POST,
                headers = mapOf(
                    "Content-Type" to "application/json"
                ),
                body = json
            )
        )

        val parser: Parser<JsonObject> = jsonObjectParser()
        val parseResult = parser.parse(res.body)

        return ((parseResult as? ParserResult.Success)?.value?.map["access_token"] as? JsonString)?.string ?: throw NullPointerException("Error parsing token")
    } catch (e: Exception) {
        throw e
    }
}