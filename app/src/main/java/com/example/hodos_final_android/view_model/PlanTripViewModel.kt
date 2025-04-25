package com.example.hodos_final_android.view_model

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hodos_final_android.model.PlanTripQuestionResponse
import com.example.hodos_final_android.repository.PlanTripRepository
import com.example.hodos_final_android.service.api.parseJsonError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanTripViewModel @Inject constructor(
    private val repository: PlanTripRepository
) : ViewModel() {

    private val _planTripQuestionState = MutableStateFlow(ResponseDataState<List<PlanTripQuestionResponse>>(isLoading = true))
    val planTripQuestionState: StateFlow<ResponseDataState<List<PlanTripQuestionResponse>>> = _planTripQuestionState

    fun loadQuestionToCollect() {
        repository.loadQuestionToCollect()
            .onEach { result ->
                _planTripQuestionState.value = when (result) {
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
