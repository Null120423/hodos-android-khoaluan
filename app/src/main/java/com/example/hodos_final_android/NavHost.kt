package com.example.hodos_final_android


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hodos_final_android.component.LoadingDialog
import com.example.hodos_final_android.screen.MainScreen
import com.example.hodos_final_android.screen.auth.LoginScreen
import com.example.hodos_final_android.screen.auth.RegisterScreen
import com.example.hodos_final_android.screen.main.planing.CreatePlanning
import com.example.hodos_final_android.screen.main.planing.PlanningDetail
import com.example.hodos_final_android.screen.main.planing.PlanningScreen
import com.example.hodos_final_android.screen.main.planing.ReviewSummaryCreatePlanningScreen
import com.example.hodos_final_android.screen.main.planing.SuggestTripScreen
import com.example.hodos_final_android.screen.start.CollectInformationScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("home")
    object Planning : Screen("planning")
    object CollectInfo : Screen("collectInfo")
    object PlanningDetail : Screen("planningDetail")
    object CreatePlanning : Screen("createPlanning")
    object ReviewSummaryCreatePlanningScreen : Screen("reviewSummaryCreatePlanningScreen")
    object SuggestTrip : Screen("suggestTrip")
}

data class ScreenConfig(
    val route: String,
    val content: @Composable () -> Unit
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(navController: NavHostController) {
    // Define transition animations
    val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)? = {
        slideInHorizontally(
            initialOffsetX = { it }, // Move from right to left
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(500))
    }

    val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition)? = {
        slideOutHorizontally(
            targetOffsetX = { -it }, // Move from left to right
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(400))
    }

    val popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)? = {
        slideInHorizontally(
            initialOffsetX = { -it }, // Move from left to right on back navigation
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(500))
    }

    val popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition)? = {
        slideOutHorizontally(
            targetOffsetX = { it }, // Move from right to left on back navigation
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(400))
    }

    // List of screen configurations
    val screens = listOf(
        ScreenConfig(Screen.Main.route) { MainScreen() },
        ScreenConfig(Screen.CollectInfo.route) { CollectInformationScreen() },
        ScreenConfig(Screen.Login.route) { LoginScreen() },
        ScreenConfig(Screen.Register.route) { RegisterScreen() },
        ScreenConfig(Screen.Planning.route) { PlanningScreen() },
        ScreenConfig(Screen.PlanningDetail.route) { PlanningDetail() },
        ScreenConfig(Screen.CreatePlanning.route) { CreatePlanning() },
        ScreenConfig(Screen.ReviewSummaryCreatePlanningScreen.route) { ReviewSummaryCreatePlanningScreen() },
        ScreenConfig(Screen.SuggestTrip.route) { SuggestTripScreen() }
    )

    NavHost(navController = navController, startDestination = Screen.CreatePlanning.route) {
        screens.forEach { screen ->
            animatedComposable(
                route = screen.route,
                enterTransition = enterTransition,
                exitTransition = exitTransition,
                popEnterTransition = popEnterTransition,
                popExitTransition = popExitTransition
            ) {
                screen.content()
            }
        }
    }
}

// Fixed extension function with correct nullable types
fun NavGraphBuilder.animatedComposable(
    route: String,
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)? = null,
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition)? = null,
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)? = null,
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition)? = null,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = content
    )
}

// Helper extension for navigation
fun NavController.navigateWithAnimation(route: String) {
    this.navigate(route) {
        launchSingleTop = true
    }
}

@Composable
fun ParentScreen(
    viewModel: LoadingViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        content()
        // Hiển thị LoadingDialog nếu đang loading
        LoadingDialog(isLoading)
    }
}


