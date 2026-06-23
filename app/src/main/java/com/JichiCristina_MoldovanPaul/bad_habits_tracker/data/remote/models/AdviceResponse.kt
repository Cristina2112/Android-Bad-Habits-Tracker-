package com.jichicristina_moldovanpaul.bad_habits_tracker.data.remote.models

data class AdviceResponse(
    val slip: Slip
)

data class Slip(
    val id: Int,
    val advice: String
)
