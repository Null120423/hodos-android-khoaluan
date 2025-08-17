package com.hodos.hodos_final_android.screen.planing

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hodos.hodos_final_android.LocalNavController
import com.hodos.hodos_final_android.component.Header
import com.hodos.hodos_final_android.component.Loading
import com.hodos.hodos_final_android.di.PlanTripModelEntryPoint
import com.hodos.hodos_final_android.model.Trip
import com.hodos.hodos_final_android.model.TripActivity
import com.hodos.hodos_final_android.model.TripDirection
import com.hodos.hodos_final_android.model.decodePolyline
import com.hodos.hodos_final_android.screen.planing.components.TripActivityCarousel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TripDirectionScreen() {
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val navController = LocalNavController.current
    val tripData = navController.previousBackStackEntry?.savedStateHandle?.get<Trip>("trip")
    val tripDirect = navController.previousBackStackEntry?.savedStateHandle?.get<TripDirection>("TripDirection")
    val planTripViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, PlanTripModelEntryPoint::class.java)
            .planTripModel()
    }

    val tripDirectionState by planTripViewModel.tripDirectionState.collectAsState()

    val tripDirection = tripDirect
        ?: tripDirectionState.data


    val hasLocationPermission = locationPermissionState.status.isGranted

    val allActivities = tripData?.days.orEmpty().flatMap { it.activities.orEmpty() }
    var firstLatLng = allActivities.firstOrNull()?.coordinates
        ?.split(",")
        ?.mapNotNull { it.toDoubleOrNull() }
        ?.takeIf { it.size == 2 }
        ?.let { LatLng(it[0], it[1]) }
        ?: LatLng(0.0, 0.0)

    val selectedActivity = remember { mutableStateOf<TripActivity?>(null) }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(firstLatLng, 14f)
    }


    LaunchedEffect(allActivities) {
        firstLatLng = allActivities.firstOrNull()?.coordinates
            ?.split(",")
            ?.mapNotNull { it.toDoubleOrNull() }
            ?.takeIf { it.size == 2 }
            ?.let { LatLng(it[0], it[1]) }
            ?: LatLng(0.0, 0.0)

        cameraPositionState.animate(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(firstLatLng, 18f)
            ),
            durationMs = 1000
        )
    }


    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        if (tripData != null && tripDirect == null) {
            planTripViewModel.tripDirection(tripData)
        }
    }

    LaunchedEffect(selectedActivity.value) {
        selectedActivity.value?.coordinates?.let { coordStr ->
            val parts = coordStr.split(",")
            if (parts.size == 2) {
                val lat = parts[0].toDoubleOrNull() ?: return@let
                val lng = parts[1].toDoubleOrNull() ?: return@let
                val newLatLng = LatLng(lat, lng)
                cameraPositionState.animate(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(
                            newLatLng,
                            18f
                        )
                    ),
                    durationMs = 1000
                )
            }
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
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
            )
        ) {

            if (tripDirection != null) {
                Polyline(
                    points = decodePolyline(tripDirection.geometry),
                    color = MaterialTheme.colorScheme.primary,
                    width = 20f,
                    pattern = listOf(
                        Dot(), Gap(10f)
                    )
                )
            }

            // Add markers for waypoints
            allActivities.forEachIndexed { _, activity ->
                activity.let { ac ->
                    val icon = rememberMarkerIconFromUrl(activity.img)
                    val (latStr, lngStr) = ac.coordinates.split(",")
                    val latLng = LatLng(latStr.toDouble(), lngStr.toDouble())

                    Marker(
                        state = MarkerState(latLng),
                        title = ac.name,
                        icon = icon,
                    )
                }
            }

        }

        if (tripDirectionState.isLoading) {
            Loading()
        }

        if (tripData != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)  // For Box
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                TripActivityCarousel(tripActivities = allActivities,
                    onActivitySelected = { activity ->
                        Log.i("API", "SELECT")
                        selectedActivity.value = activity
                    }
                )
            }
        }

        Header()
    }
}


fun Bitmap.toCircleBitmap(): Bitmap {
    val size = minOf(width, height)
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val paint = Paint().apply {
        isAntiAlias = true
        shader = BitmapShader(this@toCircleBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    val radius = size / 2f
    canvas.drawCircle(radius, radius, radius, paint)

    return output
}

@Composable
fun rememberMarkerIconFromUrl(
    url: String,
    sizeDp: Dp = 60.dp
): BitmapDescriptor? {
    val density = LocalDensity.current
    var bitmapDescriptor by remember { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(url) {
        val descriptor = withContext(Dispatchers.IO) {
            try {
                URL(url).openStream().use { inputStream ->
                    val originalBitmap = BitmapFactory.decodeStream(inputStream)
                    if (originalBitmap != null) {
                        val sizePx = with(density) { sizeDp.toPx().toInt() }
                        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, sizePx, sizePx, true)
                        val circularBitmap = resizedBitmap.toCircleBitmap()
                        BitmapDescriptorFactory.fromBitmap(circularBitmap)
                    } else null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        bitmapDescriptor = descriptor
    }

    return bitmapDescriptor
}


