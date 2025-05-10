package com.example.hodos_final_android.view_model

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationLocation
import com.example.hodos_final_android.model.PaginationResponse
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


    private val _paginationState = MutableStateFlow(ResponseDataState<PaginationResponse<Location>>(isLoading = true))
    val paginationState: StateFlow<ResponseDataState<PaginationResponse<Location>>> = _paginationState

    private val _locationFindByLabelState = MutableStateFlow(ResponseDataState<Location>(isLoading = true))
    val locationFindByLabelState : StateFlow<ResponseDataState<Location>> = _locationFindByLabelState

    private val _locationDetailState = MutableStateFlow(ResponseDataState<Location>(isLoading = true))
    val locationDetailState : StateFlow<ResponseDataState<Location>> = _locationDetailState

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

    fun findByLabel(label: String) {
        repository.findByLabel(label)
            .onEach { result ->
                _locationFindByLabelState.value = when (result) {
                    is Resource.Success -> {
                        ResponseDataState(data = result.data)
                    }

                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }

                    is Resource.Loading -> {
                        ResponseDataState(isLoading = true)
                    }

                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }

    fun detail(id: String) {
        repository.detail(id)
            .onEach { result ->
                _locationDetailState.value = when (result) {
                    is Resource.Success -> {
                        result.data?.let { Log.i("API", it.name) }
                        ResponseDataState(data = result.data)
                    }

                    is Resource.Error -> {
                        result.message?.let { Log.d("API_ERROR", it) }
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }

                    is Resource.Loading -> {
                        ResponseDataState(isLoading = true)
                    }

                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }


}

