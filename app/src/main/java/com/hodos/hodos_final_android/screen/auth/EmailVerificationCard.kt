package com.hodos.hodos_final_android.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EmailVerificationCard(
    isVisible: Boolean = true,
    userEmail: String = "",
    onVerifyClick: () -> Unit,
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var cardVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            cardVisible = true
            kotlinx.coroutines.delay(200)
            contentVisible = true
        } else {
            contentVisible = false
            kotlinx.coroutines.delay(200)
            cardVisible = false
        }
    }

    // Use AnimatedVisibility with explicit receiver
    androidx.compose.animation.AnimatedVisibility(
        visible = cardVisible && isVisible,
        enter = slideInVertically(
            animationSpec = tween(500, easing = FastOutSlowInEasing),
            initialOffsetY = { -it }
        ) + fadeIn(
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        ),
        exit = slideOutVertically(
            animationSpec = tween(400, easing = FastOutSlowInEasing),
            targetOffsetY = { -it }
        ) + fadeOut(
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                ,
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFF6B35).copy(alpha = 0.9f),
                                Color(0xFFFF8E53).copy(alpha = 0.9f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                // Close button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .offset(x = 8.dp, y = (-8).dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Content with proper Column scope
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Use AnimatedVisibility within Column scope
                    androidx.compose.animation.AnimatedVisibility(
                        visible = contentVisible,
                        enter = slideInVertically(
                            animationSpec = tween(400, delayMillis = 200, easing = FastOutSlowInEasing),
                            initialOffsetY = { it / 3 }
                        ) + fadeIn(
                            animationSpec = tween(400, delayMillis = 200, easing = FastOutSlowInEasing)
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Warning icon with pulsing animation
                            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                            val scale by infiniteTransition.animateFloat(
                                initialValue = 1f,
                                targetValue = 1.1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1000, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "scale"
                            )

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.2f),
                                        RoundedCornerShape(30.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = "Warning",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .graphicsLayer(scaleX = scale, scaleY = scale),
                                    tint = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Title with typing animation
                            AnimatedContent(
                                targetState = "Email Verification Required",
                                transitionSpec = {
                                    slideInVertically { it } + fadeIn() togetherWith
                                            slideOutVertically { -it } + fadeOut()
                                },
                                label = "title_animation"
                            ) { title ->
                                Text(
                                    text = title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Description
                            Text(
                                text = if (userEmail.isNotEmpty()) {
                                    "Please verify your email address\n$userEmail to secure your account"
                                } else {
                                    "Please verify your email address\nto secure your account and unlock all features"
                                },
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Verify button with animation
                            AnimatedVerifyButton(
                                onClick = onVerifyClick,
                                isVisible = contentVisible
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedVerifyButton(
    onClick: () -> Unit,
    isVisible: Boolean
) {
    var buttonVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            kotlinx.coroutines.delay(400)
            buttonVisible = true
        }
    }

    // Use explicit AnimatedVisibility
    androidx.compose.animation.AnimatedVisibility(
        visible = buttonVisible,
        enter = scaleIn(
            animationSpec = tween(400, easing = FastOutSlowInEasing),
            initialScale = 0.8f
        ) + fadeIn(
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFFFF6B35)
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Verify Email Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Arrow animation
                val infiniteTransition = rememberInfiniteTransition(label = "arrow")
                val arrowOffset by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 4f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "arrow_offset"
                )

                Text(
                    text = "→",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = arrowOffset.dp)
                )
            }
        }
    }
}

// Alternative compact version for smaller spaces
@Composable
fun CompactEmailVerificationCard(
    isVisible: Boolean = true,
    onVerifyClick: () -> Unit,
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    androidx.compose.animation.AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(400, easing = FastOutSlowInEasing),
            initialOffsetY = { -it / 2 }
        ) + fadeIn(
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        ),
        exit = slideOutVertically(
            animationSpec = tween(300, easing = FastOutSlowInEasing),
            targetOffsetY = { -it / 2 }
        ) + fadeOut(
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFF6B35).copy(alpha = 0.1f)
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color(0xFFFF6B35).copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color(0xFFFF6B35),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Email not verified",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B35)
                    )
                    Text(
                        text = "Tap to verify your email",
                        fontSize = 12.sp,
                        color = Color(0xFFFF6B35).copy(alpha = 0.8f)
                    )
                }

                TextButton(
                    onClick = onVerifyClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFFF6B35)
                    )
                ) {
                    Text(
                        text = "Verify",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color(0xFFFF6B35).copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// Simple banner version without complex animations
@Composable
fun SimpleEmailVerificationBanner(
    isVisible: Boolean = true,
    userEmail: String = "",
    onVerifyClick: () -> Unit,
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFF6B35)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Email Verification Required",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (userEmail.isNotEmpty()) {
                        Text(
                            text = "Verify $userEmail",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                Button(
                    onClick = onVerifyClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFFFF6B35)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Verify",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// Usage example in ProfileScreen
@Composable
fun ProfileScreenExample(
    userEmail: String = "user@example.com",
    isEmailVerified: Boolean = false,
    onNavigateToVerification: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Show verification card if email is not verified
        if (!isEmailVerified) {
            EmailVerificationCard(
                isVisible = true,
                userEmail = userEmail,
                onVerifyClick = onNavigateToVerification,
                onDismiss = { /* Handle dismiss - maybe save preference */ }
            )
        }

        // Rest of profile content
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Profile Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                // Add other profile content here
            }
        }
    }
}
