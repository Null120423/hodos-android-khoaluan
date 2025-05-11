package com.example.hodos_final_android


import GalleryFullScreen
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
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
import com.example.hodos_final_android.screen.auth.EmailVerificationScreen
import com.example.hodos_final_android.screen.auth.LoginScreen
import com.example.hodos_final_android.screen.auth.RegisterScreen
import com.example.hodos_final_android.screen.predict.PredictScreen
import com.example.hodos_final_android.screen.chat.ChatDashboard
import com.example.hodos_final_android.screen.chat.ChatRoomScreen
import com.example.hodos_final_android.screen.planing.CreatePlanning
import com.example.hodos_final_android.screen.planing.EditPlanning
import com.example.hodos_final_android.screen.planing.PlanningDetail
import com.example.hodos_final_android.screen.planing.PlanningScreen
import com.example.hodos_final_android.screen.search.SearchScreen
import com.example.hodos_final_android.model.RegisterModel
import com.example.hodos_final_android.screen.ComingSoonScreen
import com.example.hodos_final_android.screen.predict.PredictResultScreen
import com.example.hodos_final_android.screen.location.DirectionScreen
import com.example.hodos_final_android.screen.location.LocationDetailScreen
import com.example.hodos_final_android.screen.location.TourScreen
import com.example.hodos_final_android.screen.planing.CreatePlanningResultScreen
import com.example.hodos_final_android.screen.planing.DetailTripScreen
import com.example.hodos_final_android.screen.planing.TripDirectionScreen
import com.example.hodos_final_android.screen.post.PostDetailScreen
import com.google.gson.Gson
import java.net.URLEncoder

sealed class Screen(val route: String) {
    object Main : Screen("Main")
    object Login : Screen("login")
    object Register : Screen("register")
    object EmailVerification : Screen("emailVerification/{registerModel}") {
        fun createRoute(registerModel: RegisterModel): String {
            val json = Gson().toJson(registerModel)
            Log.i("JSON", json)
            return "emailVerification/$json"
        }
    }
    object Gallery : Screen("Gallery")
    object Planning : Screen("planning")
    object CollectInfo : Screen("collectInfo")
    object PlanningDetail : Screen("planningDetail")
    object CreatePlanning : Screen("createPlanning")
    object CreatePlanningResultScreen : Screen("CreatePlanningResultScreen")
    object EditPlanning : Screen("editPlanning")
    object ChatAiDashBoard : Screen("ChatAiDashBoard")
    object ChatAiRoom : Screen("ChatAiRoom")
    object SearchScreen : Screen("SearchScreen")
    object PredictScreen : Screen("PredictScreen")
    object LocationDetailScreen : Screen("LocationDetailScreen/{locationId}") {
        fun createRoute(id: String): String {
            return "LocationDetailScreen/${URLEncoder.encode(id, "UTF-8")}"
        }
    }
    object PredictResultScreen : Screen("predict_result_screen/{label}") {
        fun createRoute(label: String): String {
            return "predict_result_screen/${URLEncoder.encode(label, "UTF-8")}"
        }
    }
    object ComingSoonScreen : Screen("ComingSoonScreen")
    object Direction : Screen("Direction")
    object TripDirectionScreen : Screen("TripDirectionScreen")
    object PostDetailScreen : Screen("PostDetailScreen")
    object TourScreen : Screen("TourScreen")
    object TripDetailScreen : Screen("TripDetailScreen/{id}") {
        fun createRoute(id: String): String {
            return "TripDetailScreen/${URLEncoder.encode(id, "UTF-8")}"
        }
    }
}

data class ScreenConfig(
    val route: String,
    val content: @Composable (NavBackStackEntry) -> Unit
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(navController: NavHostController) {
    // Define transition animations
    val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)? = {
        slideInHorizontally(
            initialOffsetX = { it }, // Move from right to left
            animationSpec = tween(500)
        )
    }

    val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition)? = {
        slideOutHorizontally(
            targetOffsetX = { -it }, // Move from left to right
            animationSpec = tween(500)
        )
    }

    val popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)? = {
        slideInHorizontally(
            initialOffsetX = { -it }, // Move from left to right on back navigation
            animationSpec = tween(500)
        )
    }

    val popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition)? = {
        slideOutHorizontally(
            targetOffsetX = { it }, // Move from right to left on back navigation
            animationSpec = tween(500)
        )
    }

    // List of screen configurations
    val screens = listOf(
        ScreenConfig(Screen.Main.route) { MainScreen() },
        ScreenConfig(Screen.Login.route) { LoginScreen() },
        ScreenConfig(Screen.Register.route) { RegisterScreen() },
        ScreenConfig(Screen.Planning.route) { PlanningScreen() },
        ScreenConfig(Screen.PlanningDetail.route) { PlanningDetail() },
        ScreenConfig(Screen.CreatePlanning.route) { CreatePlanning() },
        ScreenConfig(Screen.CreatePlanningResultScreen.route) { CreatePlanningResultScreen() },
        ScreenConfig(Screen.EditPlanning.route) { EditPlanning() },
        ScreenConfig(Screen.ChatAiDashBoard.route) { ChatDashboard() },
        ScreenConfig(Screen.ChatAiRoom.route) { ChatRoomScreen() },
        ScreenConfig(Screen.SearchScreen.route) { SearchScreen() },
        ScreenConfig(Screen.EmailVerification.route) { backStackEntry ->
            val registerModelJson = backStackEntry.arguments?.getString("registerModel") ?: ""

            EmailVerificationScreen(
            registerModelJson = registerModelJson,
        ) },
        ScreenConfig(Screen.LocationDetailScreen.route) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("locationId") ?: ""
            LocationDetailScreen(
                navController = navController,
                locationId = locationId,
            )
        },
        ScreenConfig(Screen.PredictScreen.route) { PredictScreen(navController = navController) },
        ScreenConfig(Screen.PredictResultScreen.route) { backStackEntry ->
            val label = backStackEntry.arguments?.getString("label") ?: ""
            Log.i("API", label)
            PredictResultScreen(
                navController = navController,
                label = label
            )
        },
        ScreenConfig(Screen.ComingSoonScreen.route) {
            ComingSoonScreen() },
        ScreenConfig(Screen.Gallery.route ) { backStackEntry ->
            GalleryFullScreen()
        }

            ,
        ScreenConfig(Screen.Direction.route ) { backStackEntry ->
            DirectionScreen()
        },
        ScreenConfig(Screen.TripDirectionScreen.route ) { backStackEntry ->
            TripDirectionScreen()
        },

        ScreenConfig(Screen.PostDetailScreen.route ) { backStackEntry ->
            PostDetailScreen()
        },

        ScreenConfig(Screen.TourScreen.route ) { backStackEntry ->
            TourScreen()
        },
        ScreenConfig(Screen.TripDetailScreen.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailTripScreen(
                id = id,
            )
        },

    )

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        screens.forEach { screen ->
            animatedComposable(
                route = screen.route,
                enterTransition = enterTransition,
                exitTransition = exitTransition,
                popEnterTransition = popEnterTransition,
                popExitTransition = popExitTransition
            ) {backStackEntry ->
                screen.content(
                    backStackEntry
                )
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

