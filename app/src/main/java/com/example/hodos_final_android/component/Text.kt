package com.example.hodos_final_android.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun Txt(
    value: String,
    color: Color = Color.Black,
    size: Int = 14,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = value,
        fontSize = size.sp,
        fontWeight = fontWeight,
        color = color,
        textAlign = textAlign
    )
}



@Composable()
fun Title(
    size: Int = 18,
    value: String,
    fontWeight: FontWeight
) {
    Txt(size = size, value = value, fontWeight =  fontWeight)
}

