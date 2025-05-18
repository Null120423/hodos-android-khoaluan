package com.example.hodos_final_android.helper

import android.content.Context
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

import java.util.*
import java.util.concurrent.TimeUnit

fun Easing.transform(from: Float, to: Float, value: Float): Float {
    return transform(((value - from) * (1f / (to - from))).coerceIn(0f, 1f))
}

operator fun PaddingValues.times(value: Float): PaddingValues = PaddingValues(
    top = calculateTopPadding() * value,
    bottom = calculateBottomPadding() * value,
    start = calculateStartPadding(LayoutDirection.Ltr) * value,
    end = calculateEndPadding(LayoutDirection.Ltr) * value
)

fun Int.toSdp(): Dp = (this * 0.9f).dp
fun Int.toSsp(): TextUnit = (this * 0.85f).sp

@Composable
fun Int.sdpGet(): Dp {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.coerceAtLeast(1)
    val screenHeight = configuration.screenHeightDp.coerceAtLeast(1)

    val scaleFactor = min(screenWidth, screenHeight) / 400f

    return (this * scaleFactor).dp
}

@Composable
private fun getFieldId(id: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(id, "dimen", context.packageName)

}

val Int.sdp: Dp
    @Composable
    get() = this.sdpGet()

@Composable
private fun Int.textSdp(density: Density): TextUnit = with(density) {
    this@textSdp.sdp.toSp()
}

val Int.textSdp: TextUnit
    @Composable get() = this.textSdp(density = LocalDensity.current)


enum class ScreenSizeType { SMALL, NORMAL, LARGE, XLARGE }

@Composable
fun getScreenHeight() = LocalConfiguration.current.screenHeightDp

@Composable
fun getScreenWidth() = LocalConfiguration.current.screenWidthDp

@Composable
fun getScreenDensity() = LocalConfiguration.current.densityDpi

@Composable
fun getScreenSizeType(): ScreenSizeType {
    val config = LocalConfiguration.current
    return when {
        config.screenWidthDp < 600 -> ScreenSizeType.SMALL
        config.screenWidthDp in 600..839 -> ScreenSizeType.NORMAL
        config.screenWidthDp in 840..1199 -> ScreenSizeType.LARGE
        else -> ScreenSizeType.XLARGE
    }
}


class OnboardingUtils(private val context: Context) {


    fun isOnboardingCompleted(): Boolean {
        return context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            .getBoolean("completed", false)
    }

    fun setOnboardingCompleted() {
        context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("completed", true)
            .apply()
    }

}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}



fun Date.getTimeAgo(): String {
    val now = Date()
    val diffInMillis = now.time - this.time

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

    return when {
        minutes < 1 -> "Vừa xong"
        minutes < 60 -> "$minutes phút trước"
        hours < 24 -> "$hours giờ trước"
        days == 1L -> "Hôm qua"
        days < 7 -> "$days ngày trước"
        else -> java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)
    }
}

