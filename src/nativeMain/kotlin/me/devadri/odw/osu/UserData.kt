package me.devadri.odw.osu

import me.devadri.odw.Constants
import me.devadri.odw.http.Http
import me.devadri.odw.http.HttpRequest
import me.devadri.odw.lib.json.ast.JsonArray
import me.devadri.odw.lib.json.ast.JsonFloat
import me.devadri.odw.lib.json.ast.JsonObject
import me.devadri.odw.lib.json.getBoolean
import me.devadri.odw.lib.json.getFloat
import me.devadri.odw.lib.json.getInteger
import me.devadri.odw.lib.json.getString
import me.devadri.odw.lib.json.parser.base.Parser
import me.devadri.odw.lib.json.parser.base.ParserResult
import me.devadri.odw.lib.json.parser.jsonArrayParser
import me.devadri.odw.lib.json.parser.jsonObjectParser
import me.devadri.odw.util.toLocaleString
import kotlin.math.roundToInt

object UserData {

    val parser: Parser<JsonObject> = jsonObjectParser()

    fun getStats(): Stats {
        val token = OsuToken.getToken()

        val res = Http.send(
            HttpRequest(
                url = "https://osu.ppy.sh/api/v2/users/${Constants.OSU_USERNAME}/${Constants.OSU_MODE}",
                headers = mapOf(
                    "Authorization" to "Bearer $token",
                    "Accept" to "application/json"
                )
            )
        )

        val parseResult = parser.parse(res.body)

        val data = ((parseResult as? ParserResult.Success)?.value)
            ?: throw IllegalArgumentException("Error parsing user data json")

        val userId = data.getInteger("id") ?: throw IllegalArgumentException("Error parsing user id")

        val profilePicture = data.getString("avatar_url")
            ?: throw IllegalArgumentException("Error parsing profile picture")
        val name = data.getString("username")
            ?: throw IllegalArgumentException("Error parsing user name")
        val country = data.getString("country.name")
            ?: throw IllegalArgumentException("Error parsing user country")
        val countryCode = data.getString("country_code")
            ?: throw IllegalArgumentException("Error parsing user flag")
        val globalPlacement = data.getInteger("statistics.global_rank")
            ?: throw IllegalArgumentException("Error parsing user global placement")
        val countryPlacement = data.getInteger("statistics.country_rank")
            ?: throw IllegalArgumentException("Error parsing user country rank")
        val overallPP = data.getFloat("statistics.pp")
            ?: throw IllegalArgumentException("Error parsing user overall PP")
        val topPlayPP = getTopPlayPP(userId, token)
            ?: throw IllegalArgumentException("Error parsing top play PP")
        val playtime = data.getInteger("statistics.play_time")
            ?: throw IllegalArgumentException("Error parsing user play time")
        val supporter = data.getBoolean("is_supporter")
            ?: throw IllegalArgumentException("Error parsing if user is supporter")
        val mode = data.getString("playmode")
            ?: Constants.OSU_MODE.ifEmpty { throw IllegalArgumentException("Error parsing user preferred mode") }

        return Stats(
            profilePicture,
            name,
            country,
            "https://flagsapi.com/$countryCode/flat/64.png",
            parseRank(globalPlacement),
            parseRank(countryPlacement),
            parsePP(overallPP),
            parsePP(topPlayPP),
            parsePlaytime(playtime),
            supporter,
            parseMode(mode)
        )
    }

    @Suppress("SimplifyBooleanWithConstants")
    private fun getTopPlayPP(userId: Int, token: String): Float? {
        val res = Http.send(
            HttpRequest(
                url = "https://osu.ppy.sh/api/v2/users/$userId/scores/best?limit=1${
                    if (Constants.OSU_MODE != "") "&mode=${Constants.OSU_MODE}" else ""
                }}",
                headers = mapOf(
                    "Authorization" to "Bearer $token",
                    "Accept" to "application/json"
                )
            )
        )

        val parser: Parser<JsonArray> = jsonArrayParser()
        val parseResult = parser.parse(res.body)

        val data = (parseResult as? ParserResult.Success)?.value?.list?.filterIsInstance<JsonObject>()
            ?: throw NullPointerException("Error parsing user data json")

        if (data.isEmpty()) return 0f

        return (data.first().map["pp"] as? JsonFloat)?.float
    }

    private fun parsePP(pp: Float): String = "${pp.roundToInt()} PP"

    private fun parsePlaytime(seconds: Int): String {
        val days = seconds / (3600 * 24)
        val hours = (seconds % (3600 * 24)) / 3600
        val minutes = (seconds % 3600) / 60

        return "${days}d ${hours}h ${minutes}m"
    }

    private fun parseRank(rank: Int): String = "#${rank.toLocaleString()}"

    private fun parseMode(mode: String): String = when (mode) {
        "osu" -> "osu!"
        "mania" -> "osu!mania"
        "taiko" -> "osu!taiko"
        "fruits" -> "osu!catch"
        else -> throw IllegalArgumentException("lmao $mode isn't a real mode")
    }
}

class Stats(
    val profilePicture: String,
    val name: String,
    val country: String,
    val flag: String,
    val globalPlacement: String,
    val countryPlacement: String,
    val overallPP: String,
    val topPlayPP: String,
    val playtime: String,
    val supporter: Boolean,
    val mode: String
)