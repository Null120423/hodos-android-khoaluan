package com.example.hodos_final_android.screen.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.component.CircularButtonWithTitle
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.Txt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun AnalysisFeature(
    onPredict : ()-> Unit
) {
    val scope = rememberCoroutineScope()

    // List of feature descriptions
    val featureDescriptions = listOf(
        "Identify famous tourist attractions from your photos",
        "Analyze and discover local specialty dishes",
        "Display detailed information about nearby restaurants",
        "Share your discoveries with friends"
    )

    // Animation states for each description
    val animatedOffsets = remember {
        List(featureDescriptions.size) { index ->
            Animatable(initialValue = 100f)
        }
    }

    val animatedAlphas = remember {
        List(featureDescriptions.size) { index ->
            Animatable(initialValue = 0f)
        }
    }

    // Start animations with delays
    LaunchedEffect(key1 = true) {
        featureDescriptions.forEachIndexed { index, _ ->
            scope.launch {
                delay(100L * index)
                animatedOffsets[index].animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = EaseOutQuad
                    )
                )
                animatedAlphas[index].animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = EaseOutQuad
                    )
                )
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Identify Image",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            // Green subtitle
            Text(
                text = "Smart!",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Feature descriptions with animations
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                featureDescriptions.forEachIndexed { index, description ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = animatedOffsets[index].value.dp)
                            .alpha(animatedAlphas[index].value)
                            .padding(vertical = 8.dp)
                    ) {
                        Txt(
                            value = description,
                            size = 18,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Seprate(height = 32)
            CircularButtonWithTitle(
                value = "Classical Right Now",
                onClick ={
                    onPredict()
                }
            )

        }
    }
}

// Helper extension function for animation
fun Modifier.alpha(alpha: Float) = this.then(
    Modifier.graphicsLayer(alpha = alpha)
)

