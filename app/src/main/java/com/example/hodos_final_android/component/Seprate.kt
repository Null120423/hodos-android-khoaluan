package com.example.hodos_final_android.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hodos_final_android.helper.toSdp

@Composable
fun Seprate(
    height: Int = 10,
    width: Int = -1
) {
    if(width == -1) {
        Spacer(modifier = Modifier.height(height.toSdp()).fillMaxWidth())
    }else {
        Spacer(modifier = Modifier.height(height.toSdp()).width(width.toSdp()))

    }
}