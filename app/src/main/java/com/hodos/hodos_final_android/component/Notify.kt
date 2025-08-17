package com.hodos.hodos_final_android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hodos.hodos_final_android.R
import kotlinx.coroutines.delay

enum class NotifyType {
    SUCCESS, ERROR
}

@Composable
fun NotifyPopup(
    message: String,
    type: NotifyType,
    onDismiss: () -> Unit,
    durationMillis: Long = 2000
) {
    // Tự động đóng sau vài giây
    LaunchedEffect(Unit) {
        delay(durationMillis)
        onDismiss()
    }

    Dialog(onDismissRequest = { onDismiss() },
        ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
        ) {
            ColumnCenter {
                if (type == NotifyType.ERROR) {
                    AnimateImg(
                        source = R.raw.error,
                        modifier = Modifier.height(250.dp).background(Color.Transparent),
                        playOnce = true
                    )
                } else {
                    AnimateImg(
                        source = R.raw.success,
                        modifier = Modifier.height(200.dp).background(Color.Transparent),
                        playOnce = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Txt(value = message, size = 20, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background)
            }
        }
    }

}

