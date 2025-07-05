package com.example.hodos_final_android.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hodos_final_android.component.BottomBarComponent
import com.example.hodos_final_android.screen.home.HomeScreen
import com.example.hodos_final_android.screen.planing.TripUserScreen
import com.example.hodos_final_android.screen.profile.ProfileScreen
import com.example.hodos_final_android.screen.post.PostScreen
import com.example.hodos_final_android.theme.HodosTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    HodosTheme {
        Scaffold(
            bottomBar = { BottomBarComponent(navController) },
            modifier = Modifier.background(Color.Transparent).fillMaxSize().navigationBarsPadding()
        ) { _ ->

                Box(modifier = Modifier.padding()) {
                    NavigationGraph(navController)
                }

        }
    }
}

sealed class BottomBarRoute(val route: String) {
    object Home : BottomBarRoute("home")
    object Post : BottomBarRoute("Post")
    object Event : BottomBarRoute("Event")
    object Trip : BottomBarRoute("trip")
    object Profile : BottomBarRoute("Profile")
}


@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(BottomBarRoute.Home.route) { HomeScreen() }
        composable(BottomBarRoute.Post.route) { PostScreen() }
        composable(BottomBarRoute.Event.route) { FeatureDiscoveryScreen() }
        composable(BottomBarRoute.Trip.route) { TripUserScreen(
        ) }
        composable(BottomBarRoute.Profile.route) { ProfileScreen() }
    }
}