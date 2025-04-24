package com.example.hodos_final_android.view_model

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hodos_final_android.model.ChatBotResponse
import com.example.hodos_final_android.model.ChatWithBotBody
import com.example.hodos_final_android.model.SuggestQuestion
import com.example.hodos_final_android.repository.ChatBotRepository
import com.example.hodos_final_android.service.api.parseJsonError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatBotRepository
) : ViewModel() {

    private val _chatState = MutableStateFlow(ResponseDataState<ChatBotResponse>(isLoading = true))
    val chatState: StateFlow<ResponseDataState<ChatBotResponse>> = _chatState

    private val _suggestQuestionState = MutableStateFlow(ResponseDataState<List<SuggestQuestion>>(isLoading = true))
    val suggestQuestionState: StateFlow<ResponseDataState<List<SuggestQuestion>>> = _suggestQuestionState

    fun chatDashboard() {
        repository.chatDashboard()
            .onEach { result ->
                _suggestQuestionState.value = when (result) {
                    is Resource.Success -> {
                        delay(1000)
                        ResponseDataState(data = result.data)
                    }
                    is Resource.Error -> {
                        result.message?.let { Log.i("API", it) }
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }
                    is Resource.Loading -> ResponseDataState(isLoading = true)
                    else -> {
                        ResponseDataState()
                    }
                }
            }
            .launchIn(viewModelScope)
        }

        fun chatBox(body: ChatWithBotBody) {
            repository.chatBox(body)
                .onEach { result ->
                    _chatState.value = when (result) {
                        is Resource.Success -> {
                            delay(1000)
                            ResponseDataState(data = result.data)
                        }
                        is Resource.Error -> {
                            result.message?.let { Log.i("API", it) }
                            val error = result.message?.let { parseJsonError(it) }
                            ResponseDataState(error = error)
                        }
                        is Resource.Loading -> ResponseDataState(isLoading = true)
                        else -> {
                            ResponseDataState()
                        }
                    }
                }
                .launchIn(viewModelScope)
    }
}
