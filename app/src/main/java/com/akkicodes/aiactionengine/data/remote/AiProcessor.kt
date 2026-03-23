package com.akkicodes.aiactionengine.data.remote

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AiProcessor {

    private val api: AiApi

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:11434/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // safe to keep
            .build()

        api = retrofit.create(AiApi::class.java)
    }

    suspend fun processNote(input: String): String {
        return try {

            val response: ResponseBody = api.generate(
                OllamaRequest(
                    model = "phi3",
                    prompt =  """
Summarize the note in 2 lines max.

Then generate exactly 3 short tags.

Rules:
- Tags must be single words
- No sentences
- No hashtags
- No explanations

Note:
$input

Output format:
Summary: ...
Tags: tag1, tag2, tag3
""".trimIndent(),
                    stream = false
                )
            )

            val raw = response.string()

            // 🔥 CLEAN AI TEXT (IMPORTANT)
            val clean = raw
                .substringAfter("\"response\":\"")
                .substringBefore("\",\"done\"")

            clean

        } catch (e: Exception) {
            "AI Error: ${e.message}"
        }
    }
}