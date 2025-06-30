package com.example.hodos_final_android.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class ErrorType {
    GENERAL,
    NETWORK,
    SERVER,
    TIMEOUT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    errorType: ErrorType = ErrorType.GENERAL,
    title: String = "Something went wrong",
    message: String? = null,
    onRetry: (() -> Unit)? = null,
    retryText: String = "Try Again",
    onDismiss: (() -> Unit)? = null
) {
    var isVisible by remember { mutableStateOf(false) }
    var isRetrying by remember { mutableStateOf(false) }

    // Animation states
    val cardScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "contentAlpha"
    )

    // Trigger animation on composition
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    // Get error-specific properties
    val errorConfig = getErrorConfig(errorType)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .scale(cardScale)
                .alpha(contentAlpha),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Animated Error Icon
                AnimatedVisibility(
                    visible = isVisible,
                    enter = scaleIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeIn()
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        errorConfig.iconColor.copy(alpha = 0.15f),
                                        errorConfig.iconColor.copy(alpha = 0.05f)
                                    )
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = errorConfig.icon,
                            contentDescription = null,
                            tint = errorConfig.iconColor,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Animated Title
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(durationMillis = 400, delayMillis = 200)
                    ) + fadeIn(animationSpec = tween(durationMillis = 400, delayMillis = 200))
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }

                // Animated Message
                message?.let { msg ->
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(durationMillis = 400, delayMillis = 300)
                        ) + fadeIn(animationSpec = tween(durationMillis = 400, delayMillis = 300))
                    ) {
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
                        )
                    }
                }

                // Action Buttons
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(durationMillis = 400, delayMillis = 400)
                    ) + fadeIn(animationSpec = tween(durationMillis = 400, delayMillis = 400))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Retry Button
                        onRetry?.let { retryAction ->
                            Button(
                                onClick = {
                                    isRetrying = true
                                    retryAction()
                                    // Reset retry state after a delay
                                    // You might want to handle this differently based on your retry logic
                                },
                                enabled = !isRetrying,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                shape = RoundedCornerShape(16.dp),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 8.dp
                                )
                            ) {
                                if (isRetrying) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Retrying...",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = retryText,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        // Dismiss Button (optional)
                        onDismiss?.let { dismissAction ->
                            TextButton(
                                onClick = dismissAction,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Dismiss",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Reset retry state when component recomposes
    LaunchedEffect(isRetrying) {
        if (isRetrying) {
            delay(2000) // Adjust based on your retry logic
            isRetrying = false
        }
    }
}

// Error configuration data class
private data class ErrorConfig(
    val icon: ImageVector,
    val iconColor: Color
)

// Get error-specific configuration
@Composable
private fun getErrorConfig(errorType: ErrorType): ErrorConfig {
    return when (errorType) {
        ErrorType.NETWORK -> ErrorConfig(
            icon = Icons.Default.WifiOff,
            iconColor = Color(0xFFFF6B6B)
        )
        ErrorType.SERVER -> ErrorConfig(
            icon = Icons.Default.CloudOff,
            iconColor = Color(0xFFFF8E53)
        )
        ErrorType.TIMEOUT -> ErrorConfig(
            icon = Icons.Default.ErrorOutline,
            iconColor = Color(0xFFFFD93D)
        )
        ErrorType.GENERAL -> ErrorConfig(
            icon = Icons.Default.ErrorOutline,
            iconColor = Color(0xFFEF4444)
        )
    }
}