package com.example.hodos_final_android.screen.planing

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.component.Header
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.di.PlanTripModelEntryPoint
import com.example.hodos_final_android.screen.planing.components.TripActivityCarousel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TripDirectionScreen() {
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val gson = Gson()
    val route: Route = exampleTrip
    val hasLocationPermission = locationPermissionState.status.isGranted

    val firstRoute = route.tripRoutes
    val legs = route.tripRoutes.legs ?: emptyList()

    // Extract all waypoints for markers
    val waypoints = route.waypoints ?: emptyList()

    val polylinePoints = firstRoute.geometry
    val startLocation = waypoints.firstOrNull()?.let {
        LatLng(it.location?.get(0) ?: 0.0, it.location?.get(1) ?: 0.0)
    } ?: LatLng(0.0, 0.0)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startLocation, 14f)
    }

    val context = LocalContext.current
    val planTripViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, PlanTripModelEntryPoint::class.java)
            .planTripModel()
    }
    val detailState by planTripViewModel.detailState.collectAsState()

    val trip = detailState.data

    val selectedDayIndex by remember { mutableIntStateOf(0) }


    Log.i("API", trip.toString())
    LaunchedEffect(Unit) {
        planTripViewModel.detail("24126447-1d1b-4331-9c08-124e9986cb94")
    }

    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission
            ),
            uiSettings = MapUiSettings(
                    zoomControlsEnabled = false
            )
        ) {
            // Draw the route polyline
            Polyline(
                points = decodePolyline(polylinePoints),
                color = MaterialTheme.colorScheme.primary,
                width = 20f,
                pattern = listOf(
                    Dot(), Gap(10f)
                )
            )

            // Add markers for waypoints
            waypoints.forEachIndexed { index, waypoint ->
                waypoint.location?.let { location ->
                    Marker(
                        state = MarkerState(LatLng(location[0], location[1])),
                        title = "Waypoint ${index + 1}"
                    )
                }
            }
        }

        if(detailState.isLoading){
            Loading()
        }

        if (trip != null ) {
            val currentDaysActivities = trip.days?.get(selectedDayIndex)?.activities ?: emptyList()
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)  // For Box
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                TripActivityCarousel(tripActivities = currentDaysActivities)
            }

        }

        Header()
    }
}
