package com.jichicristina_moldovanpaul.bad_habits_tracker.network.models

data class GroqRequest(
    val model: String,
    val messages: List<GroqMessage>
)

data class GroqMessage(
    val role: String,
    val content: String
)

data class GroqResponse(
    val choices: List<GroqChoice>?
)

data class GroqChoice(
    val message: GroqMessage?
)
