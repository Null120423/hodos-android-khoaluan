package com.example.hodos_final_android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
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
import com.example.hodos_final_android.helper.NetworkStateMonitor
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
    private lateinit var networkMonitor: NetworkStateMonitor
    private val onboardingUtils by lazy { OnboardingUtils(this) }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkMonitor = NetworkStateMonitor(this)
        networkMonitor.register()

        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        setContent {
            val navController = rememberNavController()
            val isNetworkAvailable by networkMonitor.isConnected.collectAsState()

            HodosTheme {
                CompositionLocalProvider(LocalNavController provides navController) {
                    Box(modifier = Modifier.fillMaxSize()){
                        if (onboardingUtils.isOnboardingCompleted()) {
                            AppNavHost(navController)
                        } else {
                            ShowOnboardingScreen()
                        }

                        Box(modifier = Modifier.fillMaxSize()) {
                            // Nội dung chính app (onboarding hoặc AppNavHost)
                            if (onboardingUtils.isOnboardingCompleted()) {
                                AppNavHost(navController)
                            } else {
                                ShowOnboardingScreen()
                            }

//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(bottom = 0.dp)
//                                    .align(Alignment.BottomCenter) // Chỉ dùng được nếu trong BoxScope
//                            ) {
//                                AnimatedVisibility(
//                                    visible = !isNetworkAvailable,
//                                    enter = slideInVertically(
//                                        initialOffsetY = { it } // Từ dưới lên
//                                    ) + fadeIn(),
//                                    exit = slideOutVertically(
//                                        targetOffsetY = { it } // Trượt xuống
//                                    ) + fadeOut()
//                                ) {
//                                    Box(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .background(MaterialTheme.colorScheme.primary)
//                                    ) {
//                                        Row(modifier = Modifier.padding(10.dp)) {
//                                            Txt(
//                                                value = "Network is not connected!",
//                                                fontWeight = FontWeight.Bold,
//                                                color = MaterialTheme.colorScheme.background
//                                            )
//                                        }
//                                    }
//                                }
//                            }
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
    override fun onDestroy() {
        super.onDestroy()
        networkMonitor.unregister()
    }
}
