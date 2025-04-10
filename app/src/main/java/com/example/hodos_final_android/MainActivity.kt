package com.example.hodos_final_android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hodos_final_android.helper.OnboardingUtils
import com.example.hodos_final_android.screen.OnboardingScreen
import com.example.hodos_final_android.theme.HodosTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("NavController not provided")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val onboardingUtils by lazy { OnboardingUtils(this) }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            val navController = rememberNavController()

            HodosTheme {
                CompositionLocalProvider(LocalNavController provides navController) {
                    Surface(color = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(WindowInsets.statusBars.asPaddingValues()),
                        ) {
                        if (onboardingUtils.isOnboardingCompleted()) {
                            AppNavHost(navController)
                        } else {
                            ShowOnboardingScreen()
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun ShowOnboardingScreen() {
        val navController = LocalNavController.current
        val scope = rememberCoroutineScope()
        var showOnboarding by remember { mutableStateOf(true) }

        AnimatedContent(
            targetState = showOnboarding,
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { it }) with
                        slideOutHorizontally(targetOffsetX = { -it }) using
                        SizeTransform(clip = false)
            }
        ) { isOnboarding ->
            if (isOnboarding) {
                OnboardingScreen {
                    onboardingUtils.setOnboardingCompleted()
                    scope.launch {
                        showOnboarding = false
                    }
                }
            } else {
                AppNavHost(navController)
            }
        }
    }
}
