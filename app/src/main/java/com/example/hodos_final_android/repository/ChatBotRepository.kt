package com.example.hodos_final_android.repository



import Resource
import com.example.hodos_final_android.model.ChatBotResponse
import com.example.hodos_final_android.model.ChatWithBotBody
import com.example.hodos_final_android.model.SuggestQuestion
import com.example.hodos_final_android.service.ChatBotService
import com.example.hodos_final_android.service.api.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatBotRepository @Inject constructor(
    private val service: ChatBotService
) {
    fun chatDashboard(): Flow<Resource<List<SuggestQuestion>>> =
        safeApiCall { service.chatDashboard() }

    fun chatBox(body: ChatWithBotBody): Flow<Resource<ChatBotResponse>> =
        safeApiCall { service.chatBox(body) }
}
