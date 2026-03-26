package com.akkicodes.aiactionengine.data.remote

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AiProcessor {

    // 🔹 API interface (jisse hum AI server se baat karenge)
    private val api: AiApi

    init {

        // 🔥 OkHttpClient → ye network handle karta hai
        // yaha hum timeout increase kar rahe hai kyuki AI response slow hota hai
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)   // connection ke liye max wait
            .readTimeout(60, TimeUnit.SECONDS)      // response read karne ke liye wait
            .writeTimeout(60, TimeUnit.SECONDS)     // data bhejne ke liye wait
            .build()

        // 🔥 Retrofit → API call banane ke liye use hota hai
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:11434/")  // ⚠️ emulator → localhost mapping
            .client(client)                    // upar wala client use hoga
            .addConverterFactory(GsonConverterFactory.create()) // request JSON me convert
            .build()

        // 🔹 Api interface ko real object me convert kar diya
        api = retrofit.create(AiApi::class.java)
    }

    // 🔥 Ye main function hai jo AI se baat karta hai
    // return type = Pair<summary, tags>
    suspend fun processNote(input: String): Pair<String, String> {
        return try {

            // 🔥 API CALL → yaha AI ko request bhej rahe hai
            val response: ResponseBody = api.generate(
                OllamaRequest(
                    model = "phi3",   // kaunsa AI model use karna hai
                    prompt = """
You are a strict AI.

TASK:
- Summarize in 2 lines max
- Generate exactly 3 tags

RULES:
- No extra text
- No hashtags
- Tags = single words
- lowercase only

FORMAT:
Summary: ...
Tags: tag1, tag2, tag3

NOTE:
$input
""".trimIndent(),
                    stream = false   // 🔥 full response ek baar me chahiye
                )
            )

            // 🔥 Raw response milta hai (JSON + extra data)
            val raw = response.string()

            // 🧠 Yaha hum JSON ke andar se sirf actual AI text nikal rahe hai
            val clean = raw
                .substringAfter("\"response\":\"")   // response ke baad ka text
                .substringBefore("\",\"done\"")      // done ke pehle tak cut

            // 🔥 Ab hum clean text ko split karenge

            // 🟢 Summary nikalna
            val summary = clean
                .substringAfter("Summary:")   // Summary ke baad ka text
                .substringBefore("Tags:")     // Tags ke pehle tak
                .trim()

            // 🟢 Tags nikalna
            val tags = clean
                .substringAfter("Tags:")  // Tags ke baad ka text
                .trim()

            // 🔥 Final output → Pair me return
            Pair(summary, tags)

        } catch (e: Exception) {

            // ❌ agar error aaya (network ya AI fail)
            // fallback return karte hai
            Pair("Error generating summary", "error")
        }
    }
}