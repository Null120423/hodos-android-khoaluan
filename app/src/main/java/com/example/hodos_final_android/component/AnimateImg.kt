package com.example.hodos_final_android.component


import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun AnimateImg(
    modifier: Modifier = Modifier,
    @RawRes source: Int,
    playOnce: Boolean = false
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(source))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = if (playOnce) 1 else LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = modifier
    )
}

