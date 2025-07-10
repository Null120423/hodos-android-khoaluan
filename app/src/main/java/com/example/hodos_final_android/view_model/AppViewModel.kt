package com.example.hodos_final_android.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton


data class AppStateModel(
    val selectTabIndex : Int
)
@Singleton
class AppViewViewModel @Inject constructor(
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
