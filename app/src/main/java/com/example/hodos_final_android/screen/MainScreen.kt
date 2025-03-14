package com.example.hodos_final_android.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.example.hodos_final_android.screen.search.SearchScreen
import com.example.hodos_final_android.theme.HodosTheme

@Composable
fun MainScreen(viewModel: LoadingViewModel = viewModel()) {
    val navController = rememberNavController()


    HodosTheme {
        Scaffold(
            bottomBar = { BottomBarComponent(navController) },
            modifier = Modifier.background(Color.Green)
        ) { paddingValues ->
            ParentScreen {
                Box(modifier = Modifier.padding(paddingValues)) {
                    NavigationGraph(navController)
                }
            }
        }
    }
}

sealed class BottomBar(val route: String) {
    object Home : BottomBar("home")
    object Activity : BottomBar("activity")
    object Search : BottomBar("search")
    object Profile : BottomBar("profile")
}


@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(BottomBar.Home.route) { HomeScreen() }
        composable(BottomBar.Activity.route) { SearchScreen() }
        composable(BottomBar.Search.route) { SearchScreen() }
        composable(BottomBar.Profile.route) { ProfileScreen() }
    }
}