package com.example.boostbonk.solana

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
data class BonkRequestBody(val from: String, val to: String, val amount: Double)

suspend fun sendBonkFunctionRequest(
    from: String,
    amount: Double
): Pair<Boolean, JSONObject?> {
    return try {
        val response = client.post("https://ekjjucdrpkqujyjhrjws.supabase.co/functions/v1/send-bonk") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVramp1Y2RycGtxdWp5amhyandzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4MTM0MzQsImV4cCI6MjA2OTM4OTQzNH0.QhYvfL__NInC44o0ApQW9FKUblAdFDH1WHVEGfAvc7I")
            setBody(BonkRequestBody(
                from,
                "32xCupbneTQyyJgssmTkGpiKSgfyMnBX9igMfQL9NRdB",
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
