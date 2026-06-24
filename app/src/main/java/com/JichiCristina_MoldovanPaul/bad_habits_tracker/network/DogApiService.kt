package com.jichicristina_moldovanpaul.bad_habits_tracker.network

import com.jichicristina_moldovanpaul.bad_habits_tracker.network.models.DogResponseItem
import retrofit2.http.GET

interface DogApiService {
    @GET("v1/images/search?limit=15")
    suspend fun getDogs(): List<DogResponseItem>
}
