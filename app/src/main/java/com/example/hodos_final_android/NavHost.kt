package com.example.hodos_final_android


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
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.example.hodos_final_android.screen.location.GalleryFullScreen
import com.example.hodos_final_android.screen.location.LocationDetailScreen
import com.example.hodos_final_android.screen.location.TourScreen
import com.example.hodos_final_android.screen.notification.NotificationDetailScreen
import com.example.hodos_final_android.screen.notification.NotificationScreen
import com.example.hodos_final_android.screen.planing.CreatePlanningResultScreen
import com.example.hodos_final_android.screen.planing.CreatingPlan
import com.example.hodos_final_android.screen.planing.DetailTripScreen
import com.example.hodos_final_android.screen.planing.TripDirectionScreen
import com.example.hodos_final_android.screen.blog.BlogDetailScreen
import com.example.hodos_final_android.screen.post.CreatePostScreen
import com.example.hodos_final_android.screen.profile.PaymentMethodScreen
import com.example.hodos_final_android.screen.profile.PaymentProcessingScreen
import com.example.hodos_final_android.screen.profile.PremiumOnboardingScreen
import com.example.hodos_final_android.screen.profile.ProfileUpdateScreen
import com.example.hodos_final_android.screen.profile.SuccessScreen
import com.example.hodos_final_android.screen.profile.TrialActivationScreen
import com.example.hodos_final_android.screen.profile.UpgradeOverviewScreen
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
    object CollectInfo : Screen("collectInfo")
    // planning
    object Planning : Screen("planning")
    object PlanningDetail : Screen("planningDetail")
    object CreatingPlan : Screen("CreatingPlan")
    object CreatePlanning : Screen("createPlanning")
    object CreatePlanningResultScreen : Screen("CreatePlanningResultScreen")
    // plannning
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
    object TourScreen : Screen("TourScreen")
    object TripDetailScreen : Screen("TripDetailScreen/{id}") {
        fun createRoute(id: String): String {
            return "TripDetailScreen/${URLEncoder.encode(id, "UTF-8")}"
        }
    }
    object NotificationScreen : Screen("NotificationScreen")
    object NotificationDetailScreen : Screen("NotificationDetailScreen")
    object CreatePostScreen : Screen("CreatePostScreen")
    // flow update account
    object UpgradeOverviewScreen : Screen("UpgradeOverviewScreen")
    object PaymentMethodScreen : Screen("PaymentMethodScreen")
    object PaymentProcessingScreen : Screen("PaymentProcessingScreen")
    object SuccessScreen : Screen("SuccessScreen")
    object TrialActivationScreen : Screen("TrialActivationScreen")
    object PremiumOnboardingScreen : Screen("PremiumOnboardingScreen")

    // profile
    object UpdateProfileScreen : Screen("UpdateProfileScreen")

    // blog
    object BlogDetailScreen: Screen("BlogDetailScreen")

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
        // plan
        ScreenConfig(Screen.Planning.route) { PlanningScreen() },
        ScreenConfig(Screen.PlanningDetail.route) { PlanningDetail() },
        ScreenConfig(Screen.CreatePlanning.route) { CreatePlanning() },
        ScreenConfig(Screen.CreatePlanningResultScreen.route) { CreatePlanningResultScreen() },
        ScreenConfig(Screen.EditPlanning.route) { EditPlanning() },
        ScreenConfig(Screen.Direction.route ) { backStackEntry ->
            DirectionScreen()
        },
        ScreenConfig(Screen.CreatingPlan.route ) { backStackEntry ->
            CreatingPlan()
        },
        // plan
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
            GalleryFullScreen()  },
        ScreenConfig(Screen.TripDirectionScreen.route ) { backStackEntry ->
            TripDirectionScreen()
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
        ScreenConfig(Screen.NotificationScreen.route ) { backStackEntry ->
            NotificationScreen()
        },
        ScreenConfig(Screen.NotificationDetailScreen.route ) { backStackEntry ->
            NotificationDetailScreen()
        },
        ScreenConfig(Screen.CreatePostScreen.route ) { backStackEntry ->
            CreatePostScreen()
        },

        // flow upgrade account
        ScreenConfig(Screen.UpgradeOverviewScreen.route ) {
                backStackEntry -> UpgradeOverviewScreen()
        },
        ScreenConfig(Screen.PaymentMethodScreen.route) {
                backStackEntry -> PaymentMethodScreen()
        },
        ScreenConfig(Screen.PaymentProcessingScreen.route) {
                backStackEntry -> PaymentProcessingScreen()
        },
        ScreenConfig(Screen.SuccessScreen.route) {
                backStackEntry -> SuccessScreen()
        },
        ScreenConfig(Screen.TrialActivationScreen.route) {
                backStackEntry -> TrialActivationScreen()
        },
        ScreenConfig(Screen.PremiumOnboardingScreen.route) {
                backStackEntry -> PremiumOnboardingScreen()
        },

        // profile

        ScreenConfig(Screen.UpdateProfileScreen.route) {
                backStackEntry -> ProfileUpdateScreen()
        },

        // blog

        ScreenConfig(Screen.BlogDetailScreen.route) {
                backStackEntry -> BlogDetailScreen()
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

fun NavController.navigateBackWithAnimation() {
    this.popBackStack()
}

fun NavController.replaceCurrentWithAnimation(route: String) {
    val currentRoute = this.currentBackStackEntry?.destination?.route

    this.navigate(route) {
        launchSingleTop = true
        currentRoute?.let {
            popUpTo(it) {
                inclusive = true // remove current route from back stack
            }
        }
    }
}

