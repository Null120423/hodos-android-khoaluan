package com.example.hodos_final_android.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LinearCardBg(
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF8B5CF6),
                        Color(0xFF3B82F6)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .offset(x = 200.dp, y = (-64).dp)
                .background(
                    Color.White.copy(alpha = 0.1f),
                    CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(96.dp)
                .offset(x = (-48).dp, y = 150.dp)
                .background(
                    Color.White.copy(alpha = 0.05f),
                    CircleShape
                )
        )
        androidx.compose.foundation.layout.Column {
            content()
        }
    }
}
