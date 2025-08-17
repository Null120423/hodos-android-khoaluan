package com.hodos.hodos_final_android.model



data class Recommendation(
    val name: String,
    val img:String,
    val address: String,
    val id : String,
    val reason: String
)
data class ChatBotResponse(
    val type: String,
    val recommendations: List<Recommendation>,
    val message: String,
    val reason: String,
    val description: String
)

data class SuggestQuestion(
    val message: String,
    val img: String
)

data class ChatWithBotBody(
    val message: String
)