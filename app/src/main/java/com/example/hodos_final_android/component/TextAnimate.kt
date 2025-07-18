package com.example.hodos_final_android.component

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

@Composable
fun AnimatedTypingText(
    fullText: String,
    delayMillis: Long = 25,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    var textToShow by remember { mutableStateOf("") }
    var hasAnimated by remember { mutableStateOf(false) }
Log.i("API", "RENDER")
    LaunchedEffect(key1 = fullText) {
        if (!hasAnimated) {
            textToShow = ""
            fullText.forEachIndexed { i, _ ->
                textToShow = fullText.substring(0, i + 1)
                delay(delayMillis)
            }
            hasAnimated = true
        } else {
            // Nếu đã từng chạy, hiển thị full text ngay
            textToShow = fullText
        }
    }

    Text(
        text = textToShow,
        style = textStyle,
        modifier = modifier
    )
}

