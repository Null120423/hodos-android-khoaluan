@file:Suppress("DEPRECATION")

package com.hodos.hodos_final_android.theme


import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF057EF4),
    secondary =   Color(0xFFF2FDFF),
    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    tertiary = Color.Black,
    scrim = dark_tran
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF057EF4),
    secondary = Color(0xFFF2FDFF),
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    tertiary = Color.White,
    scrim = dark_tran
)

val greenColor = Color(0xFF5BC359)

@Composable
fun HodosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Cho phép vẽ content dưới status bar
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // Làm status bar trong suốt
            window.statusBarColor = android.graphics.Color.TRANSPARENT

            // Đặt màu icon (trắng hoặc đen)
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars =  true
        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}



