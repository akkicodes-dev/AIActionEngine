package com.akkicodes.aiactionengine.data.remote

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

// 📦 Request body (AI ko kya bhejna hai)
data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false   // 🔥 important (no streaming)
)

// 🚫 Response class remove kar diya (kyunki hum raw string use kar rahe)
interface AiApi {

    @POST("api/generate")
    suspend fun generate(
        @Body request: OllamaRequest
    ): ResponseBody   // 🔥 raw response
}