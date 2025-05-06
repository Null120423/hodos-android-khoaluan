package com.example.hodos_final_android.model



data class Recommendation(
    val name: String,
    val reason: String,
    val images: List<String>,
    val address: String,
    val id : String
)
data class ChatBotResponse(
    val type: String,
    val recommendations: List<Recommendation>,
    val message: String
)



data class SuggestQuestion(
    val message: String,
    val img: String
)

data class ChatWithBotBody(
    val message: String
)