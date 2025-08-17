package com.hodos.hodos_final_android.component.Toast


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ToastManager {
    private val _toasts = mutableStateListOf<ToastData>()
    val toasts: List<ToastData> = _toasts

    @OptIn(DelicateCoroutinesApi::class)
    fun showToast(
        type: ToastType,
        title: String,
        message: String,
        duration: Long = 3000L,
        showButton: Boolean = false,
        buttonText: String = "Button",
        onButtonClick: () -> Unit = {}
    ) {
        val toast = ToastData(
            id = System.currentTimeMillis(),
            type = type,
            title = title,
            message = message,
            showButton = showButton,
            buttonText = buttonText,
            onButtonClick = onButtonClick
        )
        _toasts.add(toast)

        // Auto dismiss after duration
        kotlinx.coroutines.GlobalScope.launch {
            delay(duration)
            dismissToast(toast.id)
        }
    }

    fun dismissToast(id: Long) {
        _toasts.removeAll { it.id == id }
    }
}

data class ToastData(
    val id: Long,
    val type: ToastType,
    val title: String,
    val message: String,
    val showButton: Boolean = false,
    val buttonText: String = "Button",
    val onButtonClick: () -> Unit = {}
)

@Composable
fun ToastContainer(toastManager: ToastManager) {
    Box(
        modifier = Modifier.padding(top = 30.dp)
    ) {
        Column {
            toastManager.toasts.forEach { toast ->
                CustomToast(
                    type = toast.type,
                    title = toast.title,
                    message = toast.message,
                    showButton = toast.showButton,
                    buttonText = toast.buttonText,
                    onButtonClick = toast.onButtonClick,
                    onDismiss = { toastManager.dismissToast(toast.id) }
                )
            }
        }
    }
}