package com.jichicristina_moldovanpaul.bad_habits_tracker.data.remote

import com.jichicristina_moldovanpaul.bad_habits_tracker.data.remote.models.AdviceResponse
import retrofit2.http.GET

interface ApiService {
    @GET("advice")
    suspend fun getRandomAdvice(): AdviceResponse
}
