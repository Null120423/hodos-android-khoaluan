package com.hodos.hodos_final_android.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun <T> rememberDebouncedState(value: T, debounceMillis: Long = 500L): State<T> {
    val state = remember { mutableStateOf(value) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(value) {
        coroutineScope.launch {
            delay(debounceMillis)
            state.value = value
        }
    }
    return state
}
