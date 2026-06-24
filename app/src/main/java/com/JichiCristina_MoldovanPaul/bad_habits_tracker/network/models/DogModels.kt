package com.jichicristina_moldovanpaul.bad_habits_tracker.network.models

import com.google.gson.annotations.SerializedName

data class DogResponseItem(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("url")
    val url: String
)
