package com.example.hodos_final_android.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun Txt(
    value: String,
    color: Color = MaterialTheme.colorScheme.tertiary,
    size: Int = 14,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int? = null,
) {
    if (maxLines != null) {
        Text(
            text = value,
            fontSize = size.sp,
            fontWeight = fontWeight,
            color = color,
            textAlign = textAlign,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
    }else {
        Text(
            text = value,
            fontSize = size.sp,
            fontWeight = fontWeight,
            color = color,
            textAlign = textAlign
        )
    }
}



@Composable()
fun Title(
    value: String,
    fontWeight: FontWeight = FontWeight.Normal,
    size: Int = 18,
    color : Color = MaterialTheme.colorScheme.tertiary,
    textAlign: TextAlign = TextAlign.Start,
) {
    Txt(size = size, value = value, fontWeight =  fontWeight, color = color, textAlign = textAlign)
}

