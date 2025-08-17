package com.hodos.hodos_final_android.service


import com.hodos.hodos_final_android.model.ChatBotResponse
import com.hodos.hodos_final_android.model.ChatWithBotBody
import com.hodos.hodos_final_android.model.SuggestQuestion
import retrofit2.http.Body
import retrofit2.http.POST


interface ChatBotService {
    @POST("ai/chat-dashboard")
    suspend fun chatDashboard(): List<SuggestQuestion>


    @POST("ai/chat-box")
    suspend fun chatBox(@Body() body: ChatWithBotBody): ChatBotResponse
}

