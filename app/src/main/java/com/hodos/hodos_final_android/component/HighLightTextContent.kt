package com.hodos.hodos_final_android.component

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun HighlightedContent(text: String, onTagClick: (String) -> Unit) {
    val annotatedText = buildAnnotatedString {
        val regex = "#\\w+".toRegex()
        var lastIndex = 0

        for (match in regex.findAll(text)) {
            val start = match.range.first
            val end = match.range.last + 1

            append(text.substring(lastIndex, start))

            pushStringAnnotation(tag = "HASHTAG", annotation = match.value)
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(match.value)
            }
            pop()

            lastIndex = end
        }

        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }

    ClickableText(
        text = annotatedText,
        style = TextStyle(fontSize = 16.sp),
        onClick = { offset ->
            annotatedText.getStringAnnotations("HASHTAG", offset, offset)
                .firstOrNull()?.let { annotation ->
                    onTagClick(annotation.item)
                }
        }
    )
}
