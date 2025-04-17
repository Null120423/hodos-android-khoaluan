package com.example.hodos_final_android.screen.predict

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.screen.search.LocationItem
import com.example.hodos_final_android.view_model.LocationViewModel


@Composable
fun PredictResultScreen(
    navController: NavHostController,
    label: String? = "",
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val locationState by locationViewModel.locationFindByLabelState.collectAsState()

    LaunchedEffect(label) {
        label?.let {
            locationViewModel.findByLabel(it)
        }
    }

    MainLayout(
        isLoading = locationState.isLoading,
        title = "Predict result",
        content = {
            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth()
            ) {
                if(locationState.isLoading) {
                    Txt(value = "Loading...")
                }else if(locationState.error != null) {
                    Txt(value = "${(locationState.error)?.message}")
                } else if(locationState.data != null) {
                    LocationItem(
                        data = locationState.data!!,
                        onClick = {
                            navController.navigateWithAnimation(Screen.LocationDetailScreen.createRoute(
                                locationState.data!!.id))
                        }
                    )
                }else {
                    Txt(value = "Location not found!")
                }
            }

        }
    )
}

