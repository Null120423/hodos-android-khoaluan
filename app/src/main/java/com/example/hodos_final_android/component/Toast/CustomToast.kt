package com.example.hodos_final_android.component.Toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ToastType {
    ALERT, CAUTION, SUCCESS, INFO
}

@Composable
fun CustomToast(
    type: ToastType,
    title: String,
    message: String,
    showButton: Boolean = false,
    buttonText: String = "Button",
    onButtonClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val (backgroundColor, iconColor, icon, textColor, buttonColor) = when (type) {
        ToastType.ALERT -> Tuple5(
            Color(0xFFFFF5F5),
            Color(0xFFEF4444),
            Icons.Default.Close,
            Color.Black,
            Color(0xFFEF4444)
        )
        ToastType.CAUTION -> Tuple5(
            Color(0xFFFFFBEB),
            Color(0xFFF59E0B),
            Icons.Default.Warning,
            Color.Black,
            Color(0xFFF59E0B)
        )
        ToastType.SUCCESS -> Tuple5(
            Color(0xFFF0FDF4),
            Color(0xFF10B981),
            Icons.Default.Check,
            Color.Black,
            Color(0xFF10B981)
        )
        ToastType.INFO -> Tuple5(
            Color(0xFFF8FAFC),
            Color(0xFF8B5CF6),
            Icons.Default.Info,
            Color.Black,
            Color(0xFF8B5CF6)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = iconColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = textColor.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )

                if (showButton) {
                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = onButtonClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = buttonColor
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = buttonText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// Helper data class for multiple return values
data class Tuple5<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)