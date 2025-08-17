package com.hodos.hodos_final_android.view_model

import Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hodos.hodos_final_android.model.NotificationModel
import com.hodos.hodos_final_android.model.Pagination
import com.hodos.hodos_final_android.repository.NotificationRepository
import com.hodos.hodos_final_android.service.NotificationPaginationResponse
import com.hodos.hodos_final_android.service.ReadNotificationResponse
import com.hodos.hodos_final_android.service.api.parseJsonError
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationViewModel @Inject constructor(
    private val repo: NotificationRepository,
) : ViewModel() {

    private val _notificationPaginationState = MutableStateFlow(ResponseDataState<NotificationPaginationResponse>(isLoading = true))
    val notificationPaginationState: StateFlow<ResponseDataState<NotificationPaginationResponse>> = _notificationPaginationState.asStateFlow()

    private val _detailState = MutableStateFlow(ResponseDataState<NotificationModel>(isLoading = true))
    val detailState: StateFlow<ResponseDataState<NotificationModel>> = _detailState.asStateFlow()

    private val _readState = MutableStateFlow(ResponseDataState<ReadNotificationResponse>(isLoading = true))
    val readState: StateFlow<ResponseDataState<ReadNotificationResponse>> = _readState.asStateFlow()

    fun pagination(body: Pagination<Any>) {
        repo.pagination(body)
            .onEach { result ->
                _notificationPaginationState.value = when (result) {
                    is Resource.Success -> {
                        val currentData = _notificationPaginationState.value.data?.data ?: emptyList()
                        val newData = if (body.skip > 0) currentData + (result.data?.data ?: emptyList())
                        else result.data?.data ?: emptyList()
                        ResponseDataState(data = result.data?.copy(data = newData))
                    }
                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }
                    is Resource.Loading -> {
                        ResponseDataState(isLoading = true, data = _notificationPaginationState.value.data)
                    }
                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }

    fun detail(id: String) {
        repo.detail(id)
            .onEach { result ->
                _detailState.value = when (result) {
                    is Resource.Success -> {
                        ResponseDataState(data = result.data)
                    }
                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }
                    is Resource.Loading -> {
                        ResponseDataState(isLoading = true, data = _detailState.value.data)
                    }
                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }

    fun read(id: String) {
        repo.read(id)
            .onEach { result ->
                _readState.value = when (result) {
                    is Resource.Success -> {
                        ResponseDataState(data = result.data)
                    }
                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }
                    is Resource.Loading -> {
                        ResponseDataState(isLoading = true, data = _readState.value.data)
                    }
                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }
}
