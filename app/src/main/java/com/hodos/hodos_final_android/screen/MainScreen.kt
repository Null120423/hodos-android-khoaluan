package com.hodos.hodos_final_android.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hodos.hodos_final_android.component.BottomBarComponent
import com.hodos.hodos_final_android.di.AppStateViewEntryPoint
import com.hodos.hodos_final_android.navigateWithAnimation
import com.hodos.hodos_final_android.screen.home.HomeScreen
import com.hodos.hodos_final_android.screen.planing.TripUserScreen
import com.hodos.hodos_final_android.screen.post.PostScreen
import com.hodos.hodos_final_android.screen.profile.ProfileScreen
import com.hodos.hodos_final_android.theme.HodosTheme
import dagger.hilt.android.EntryPointAccessors

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val appStateViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, AppStateViewEntryPoint::class.java)
            .appStateViewModel()
    }

    val appState by appStateViewModel.appState.collectAsState()

    // Di chuyển đến tab tương ứng khi selectTabIndex thay đổi
    LaunchedEffect(appState.selectTabIndex) {
        val targetRoute = BottomBarRoute.routes.getOrNull(appState.selectTabIndex)?.route
        val currentRoute = navController.currentDestination?.route
        if (targetRoute != null && currentRoute != targetRoute) {
            navController.navigateWithAnimation(targetRoute)
        }
    }

    HodosTheme {
        Scaffold(
            bottomBar = { BottomBarComponent(navController) },
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize()
                .navigationBarsPadding()
        ) { _ ->
            Box(modifier = Modifier.padding()) {
                NavigationGraph(navController)
            }
        }
    }
}

// Điều hướng BottomBar
sealed class BottomBarRoute(val route: String) {
    object Home : BottomBarRoute("Home")
    object Trip : BottomBarRoute("Trip")
    object Event : BottomBarRoute("Event")
    object Post : BottomBarRoute("Post")
    object Profile : BottomBarRoute("Profile")

    companion object {
        val routes = listOf(Home, Trip, Event,Post, Profile)
    }
}

// Navigation graph
@Composable
fun NavigationGraph(navController: NavHostController) {
    val context = LocalContext.current

    val appStateViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, AppStateViewEntryPoint::class.java)
            .appStateViewModel()
    }

    val appState by appStateViewModel.appState.collectAsState()

    val targetRoute = BottomBarRoute.routes.getOrNull(appState.selectTabIndex)?.route
    NavHost(
        navController = navController,
        startDestination = targetRoute ?: BottomBarRoute.Home.route
    ) {
        composable(BottomBarRoute.Home.route) { HomeScreen() }
        composable(BottomBarRoute.Trip.route) { TripUserScreen() }
        composable(BottomBarRoute.Event.route) { FeatureDiscoveryScreen() }
        composable(BottomBarRoute.Post.route) { PostScreen() }
        composable(BottomBarRoute.Profile.route) { ProfileScreen() }
    }
}
