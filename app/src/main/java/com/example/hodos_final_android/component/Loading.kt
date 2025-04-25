package com.example.hodos_final_android.component

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.R

@Composable
fun Loading() {
    AnimateImg(
        source = R.raw.typing,
        modifier = Modifier.height(100.dp)
    )
}
