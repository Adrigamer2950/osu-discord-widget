package me.devadri.odw.discord

import me.devadri.odw.Constants
import me.devadri.odw.http.Http
import me.devadri.odw.http.HttpRequest
import me.devadri.odw.http.HttpRequest.Method
import me.devadri.odw.osu.UserData

object DiscordIdentity {

    const val USERNAME = "devadri"

    fun createIdentity(): String {
        val stats = UserData.getStats()

        val dynamicValues = mapOf(
            "osu_profile"            to stats.profilePicture,   // image
            "osu_name"               to stats.name,             // string
            "osu_country"            to stats.country,          // string
            "osu_flag"               to stats.flag,             // image
            "osu_global_placement"   to stats.globalPlacement,  // string
            "osu_country_placement"  to stats.countryPlacement, // string
            "osu_overall_pp"         to stats.overallPP,        // string
            "osu_top_play"           to stats.topPlayPP,        // string
            "osu_playtime"           to stats.playtime,         // string
            "osu_is_supporter"       to stats.supporter,        // string
            "osu_mode"               to stats.mode,             // string
        ).map { (key, value) ->
            val isUrl = value is String && value.contains("https")

            val type = when {
                isUrl        -> DiscordIdentityDataDynamicValueType.MEDIA
                value is String -> DiscordIdentityDataDynamicValueType.STRING
                value is Int    -> DiscordIdentityDataDynamicValueType.NUMBER
                value is Boolean -> DiscordIdentityDataDynamicValueType.STRING
                else            -> throw IllegalArgumentException(":skull: $key")
            }.decimal

            val objectValue: String = when {
                isUrl   -> """
                        { "url": "$value" }
                    """.trimIndent().replace("\n", "")
                value is String -> "\"$value\""
                value is Int -> value.toString()
                value is Boolean -> if (value) "\"Yes\"" else "\"No\""
                else -> throw IllegalArgumentException("wtf is this '$key' :skull:")
            }

            """{ "type": $type, "name": "$key", "value": $objectValue }"""
        }.toTypedArray()

        return """
{
"username": "$USERNAME",
"data": {
"dynamic": [
${ dynamicValues.joinToString(", \n") }
]
}
}
""".trimIndent().replace("\n", "")
    }

    fun createAndPatchIdentity() {
        val identity = createIdentity()

        println(identity)

        val res = Http.send(HttpRequest(
            url = "https://discord.com/api/v9/applications/${Constants.DISCORD_APP_ID}/users/${Constants.DISCORD_USER_ID}/identities/0/profile",
            method = Method.PATCH,
            headers = mapOf(
                "Authorization" to "Bot ${Constants.DISCORD_TOKEN}",
                "Content-Type" to "application/json"
            ),
            body = identity
        ))

        println("Response:")
        println("Status Code: ${res.statusCode}")
        println("Body: ${res.body}")
    }
}

enum class DiscordIdentityDataDynamicValueType(val decimal: Int) {

    STRING(1),
    NUMBER(2),
    MEDIA(3);
}