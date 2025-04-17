package com.example.hodos_final_android.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hodos_final_android.LoadingViewModel
import com.example.hodos_final_android.ParentScreen
import com.example.hodos_final_android.component.BottomBarComponent
import com.example.hodos_final_android.screen.home.HomeScreen
import com.example.hodos_final_android.screen.profile.ProfileScreen
import com.example.hodos_final_android.theme.HodosTheme

@Composable
fun MainScreen(viewModel: LoadingViewModel = viewModel()) {
    val navController = rememberNavController()


    HodosTheme {
        Scaffold(
            bottomBar = { BottomBarComponent(navController) },
            modifier = Modifier.background(Color.Transparent).fillMaxSize()
        ) { innerPadding ->
            ParentScreen {
                Box(modifier = Modifier.padding()) {
                    NavigationGraph(navController)
                }
            }
        }
    }
}

sealed class BottomBarRoute(val route: String) {
    object Home : BottomBarRoute("home")
    object Activity : BottomBarRoute("activity")
    object Trip : BottomBarRoute("trip")
    object Event : BottomBarRoute("Event")
    object Profile : BottomBarRoute("Profile")
}


@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(BottomBarRoute.Home.route) { HomeScreen() }
        composable(BottomBarRoute.Activity.route) { ComingSoonScreen() }
        composable(BottomBarRoute.Trip.route) { ComingSoonScreen(
        ) }
        composable(BottomBarRoute.Event.route) { ComingSoonScreen() }
        composable(BottomBarRoute.Profile.route) { ProfileScreen() }
    }
}