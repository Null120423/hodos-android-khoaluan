package com.hodos.hodos_final_android.view_model

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hodos.hodos_final_android.model.DashboardModel
import com.hodos.hodos_final_android.repository.CommonRepository
import com.hodos.hodos_final_android.service.api.parseJsonError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

data class GetDashBoard(
    val userId: String? = null
)
@Singleton
class HomeViewModel @Inject constructor(
    private val repository: CommonRepository,
) : ViewModel() {

    private val _homeState = MutableStateFlow(ResponseDataState<DashboardModel>(isLoading = true))
    val homeState: StateFlow<ResponseDataState<DashboardModel>> = _homeState


    fun getDashboard(userId: String?) {
        val body = GetDashBoard(
            userId = userId
        )
        repository.dashboard(body)
            .onEach { result ->
                _homeState.value = when (result) {
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
