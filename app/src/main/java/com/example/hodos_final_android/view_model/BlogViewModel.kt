package com.example.hodos_final_android.view_model

import Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hodos_final_android.model.BlogModel
import com.example.hodos_final_android.model.NotificationModel
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationResponse
import com.example.hodos_final_android.repository.BlogRepository
import com.example.hodos_final_android.repository.NotificationRepository
import com.example.hodos_final_android.service.NotificationPaginationResponse
import com.example.hodos_final_android.service.ReadNotificationResponse
import com.example.hodos_final_android.service.api.parseJsonError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlogViewModel @Inject constructor(
    private val repo: BlogRepository,
) : ViewModel() {

    private val _blogPaginationState = MutableStateFlow(ResponseDataState<PaginationResponse<BlogModel>>(isLoading = true))
    val blogPaginationState: StateFlow<ResponseDataState<PaginationResponse<BlogModel>>> = _blogPaginationState.asStateFlow()

    fun pagination(body: Pagination<Any>) {
        repo.pagination(body)
            .onEach { result ->
                _blogPaginationState.value = when (result) {
                    is Resource.Success -> {
                        val currentData = _blogPaginationState.value.data?.data ?: emptyList()
                        val newData = if (body.skip > 0) currentData + (result.data?.data ?: emptyList())
                        else result.data?.data ?: emptyList()
                        ResponseDataState(data = result.data?.copy(data = newData))
                    }
                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }
                    is Resource.Loading -> {
                        ResponseDataState(isLoading = true, data = _blogPaginationState.value.data)
                    }
                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }
}
