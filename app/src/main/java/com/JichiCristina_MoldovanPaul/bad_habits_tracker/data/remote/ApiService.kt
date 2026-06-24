package com.jichicristina_moldovanpaul.bad_habits_tracker.data.remote

import com.jichicristina_moldovanpaul.bad_habits_tracker.data.remote.models.GroqRequest
import com.jichicristina_moldovanpaul.bad_habits_tracker.data.remote.models.GroqResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("openai/v1/chat/completions")
    suspend fun generateAdvice(@Body request: GroqRequest): GroqResponse
}
