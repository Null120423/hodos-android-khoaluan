package com.example.hodos_final_android.view_model

import Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationLocation
import com.example.hodos_final_android.model.PaginationLocationRes
import com.example.hodos_final_android.repository.LocationRepository
import com.example.hodos_final_android.service.api.parseJsonError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {


    private val _paginationState = MutableStateFlow(ResponseDataState<PaginationLocationRes>(isLoading = true))
    val paginationState: StateFlow<ResponseDataState<PaginationLocationRes>> = _paginationState

    fun pagination(body: Pagination<PaginationLocation>) {
        repository.pagination(body)
            .onEach { result ->
                _paginationState.value = when (result) {
                    is Resource.Success -> {
                        val currentData = _paginationState.value.data?.data ?: emptyList()
                        val newData = if (body.skip > 0) currentData + (result.data?.data ?: emptyList())
                        else result.data?.data ?: emptyList()
                        ResponseDataState(data = result.data?.copy(data = newData))
                    }

                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }

                    is Resource.Loading -> {
                        ResponseDataState(isLoading = true, data = _paginationState.value.data)
                    }

                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }


}

