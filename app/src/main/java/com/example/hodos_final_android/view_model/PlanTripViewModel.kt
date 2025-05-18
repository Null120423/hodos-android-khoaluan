package com.example.hodos_final_android.view_model

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationResponse
import com.example.hodos_final_android.model.PlanTripQuestionResponse
import com.example.hodos_final_android.model.PlanTripRes
import com.example.hodos_final_android.model.SaveTripResponse
import com.example.hodos_final_android.model.Trip
import com.example.hodos_final_android.model.TripDirection
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

    private val _planTripQuestionState =
        MutableStateFlow(ResponseDataState<List<PlanTripQuestionResponse>>(isLoading = true))
    val planTripQuestionState: StateFlow<ResponseDataState<List<PlanTripQuestionResponse>>> =
        _planTripQuestionState


    private val _planTripResultState = MutableStateFlow(ResponseDataState<PlanTripRes>(isLoading = false))
    val planTripResultState: StateFlow<ResponseDataState<PlanTripRes>> = _planTripResultState

    private val _saveTripState = MutableStateFlow(ResponseDataState<SaveTripResponse>(isLoading = false))
    val saveTripState: StateFlow<ResponseDataState<SaveTripResponse>> = _saveTripState

    private val _paginationTripUserState = MutableStateFlow(ResponseDataState<PaginationResponse<Trip>>(isLoading = true))
    val paginationTripUserState: StateFlow<ResponseDataState<PaginationResponse<Trip>>> = _paginationTripUserState


    private val _detailState = MutableStateFlow(ResponseDataState<Trip>(isLoading = true))
    val detailState: StateFlow<ResponseDataState<Trip>> = _detailState

    private val _tripDirectionState = MutableStateFlow(ResponseDataState<TripDirection>(isLoading = false))
    val tripDirectionState: StateFlow<ResponseDataState<TripDirection>> = _tripDirectionState

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
                    is Resource.Loading -> {
                        _planTripResultState.value = ResponseDataState(isLoading = false)
                        ResponseDataState(isLoading = true, data = null)
                    }
                    else -> {
                        ResponseDataState()
                    }
                }
            }
            .launchIn(viewModelScope)
        }


    fun planTrip(body: Any){
        repository.planTrip(body)
            .onEach { result ->
                _planTripResultState.value = when (result) {
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

    fun detail(id: String){
        repository.detail(id)
            .onEach { result ->
                _detailState.value = when (result) {
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

    fun saveTrip(body: Any){
        repository.saveTrip(body)
            .onEach { result ->
                _saveTripState.value = when (result) {
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

    fun tripDirection(body: Any){
        repository.tripDirection(body)
            .onEach { result ->
                _tripDirectionState.value = when (result) {
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

    fun paginationTripUser(body: Pagination<Any>) {
        repository.paginationTripUser(body)
            .onEach { result ->
                _paginationTripUserState.value = when (result) {
                    is Resource.Success -> {
                        val currentData = _paginationTripUserState.value.data?.data ?: emptyList()
                        val newData = if (body.skip > 0) currentData + (result.data?.data ?: emptyList())
                        else result.data?.data ?: emptyList()
                        ResponseDataState(data = result.data?.copy(data = newData))
                    }

                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }

                    is Resource.Loading -> {
                        ResponseDataState(isLoading = true, data = _paginationTripUserState.value.data)
                    }

                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }

    fun clearSaveTrip() {
        _saveTripState.value = ResponseDataState(isLoading = false)
    }
}
