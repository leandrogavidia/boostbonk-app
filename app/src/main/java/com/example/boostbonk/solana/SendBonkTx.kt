package com.example.boostbonk.solana

import com.example.boostbonk.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.json.JSONObject

private val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

@Serializable
data class BonkRequestBody(
    val from: String,
    val to: String,
    val amount: Double
)

suspend fun sendBonkFunctionRequest(
    from: String,
    to: String,
    amount: Double
): Pair<Boolean, JSONObject?> {
    val token = BuildConfig.SUPABASE_AUTH_TOKEN
    return try {
        val response = client.post("https://ekjjucdrpkqujyjhrjws.supabase.co/functions/v1/send-bonk") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
            setBody(BonkRequestBody(
                from,
                to,
                amount
            ))
        }

        val bodyString = response.bodyAsText()
        val json = JSONObject(bodyString)

        Pair(response.status == HttpStatusCode.OK, json)
    }  catch (e: Exception) {
        e.printStackTrace()
        Pair(false, null)
    }
}
