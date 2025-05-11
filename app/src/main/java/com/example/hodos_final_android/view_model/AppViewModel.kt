package com.example.hodos_final_android.view_model

import androidx.lifecycle.ViewModel
import com.example.hodos_final_android.repository.CommonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton


data class AppStateModel(
    val selectTabIndex : Int
)
@Singleton
class AppViewViewModel @Inject constructor(
    private val repository: CommonRepository
) : ViewModel() {

    private val _appState = MutableStateFlow(AppStateModel(
        selectTabIndex = 0
    ))
    val appState: StateFlow<AppStateModel> = _appState

    fun onSelectTab(index: Int) {
        _appState.value = AppStateModel(
            selectTabIndex = index
        )
    }
}
