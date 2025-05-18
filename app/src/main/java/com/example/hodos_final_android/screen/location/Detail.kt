package com.example.hodos_final_android.screen.location

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.CarouselExample
import com.example.hodos_final_android.component.ColumnStart
import com.example.hodos_final_android.component.Header
import com.example.hodos_final_android.component.IconBtn
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.component.Title
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.model.LocationDetailModel
import com.example.hodos_final_android.model.Route
import com.example.hodos_final_android.model.decodePolyline
import com.example.hodos_final_android.model.exampleRouteData
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.view_model.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationDetailScreen(
    navController: NavController,
    locationId: String? = null,
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val locationDetailState by locationViewModel.locationDetailState.collectAsState()

    LaunchedEffect(locationId) {
        locationId?.let {
            Log.i("API", locationId)
            locationViewModel.detail(locationId)
        }
    }

    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary).fillMaxSize()
    ) {
        if(locationDetailState.error !== null) {
            Column {
                Title(value = locationDetailState.error!!.message, fontWeight = FontWeight.Bold)
            }
        }

        if(locationDetailState.data != null) {
            CoxsBazarBeachInfo(data = locationDetailState.data!!)
        }

    }
    if(locationDetailState.isLoading ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Loading()
        }
    }

    Header()


}


@Composable
fun CoxsBazarBeachInfo(data: Location) {
    val scrollState = rememberScrollState()
    val navController = LocalNavController.current



   Box{
       Column(
           modifier = Modifier
               .fillMaxSize()
               .verticalScroll(scrollState)
               .background(MaterialTheme.colorScheme.background)
       ) {
           Box{
               CarouselExample(rounded = 0, height = 300, banners = data.lstImgs.take(4))



           }
           // Header section with beach name and location
           ColumnStart(modifier = Modifier.padding(10.dp)) {
               Text(
                   text = data.name,
                   style = MaterialTheme.typography.headlineMedium.copy(
                       fontWeight = FontWeight.Bold
                   ),
               )

               Row(
                   verticalAlignment = Alignment.CenterVertically,
                   modifier = Modifier.padding(vertical = 8.dp)
               ) {
                   Icon(
                       Icons.Filled.Place,
                       contentDescription = "Location",
                       modifier = Modifier.size(18.dp)
                   )
                   Spacer(modifier = Modifier.width(4.dp))
                   Text(
                       text = data.address,
                       color = MaterialTheme.colorScheme.onSurfaceVariant,
                       fontSize = 16.sp
                   )
               }

               // Rating and visitors section
               Row(
                   verticalAlignment = Alignment.CenterVertically,
                   modifier = Modifier.padding(bottom = 16.dp)
               ) {
                   Row(verticalAlignment = Alignment.CenterVertically) {
                       Icon(
                           Icons.Filled.Star,
                           contentDescription = "Rating",
                           tint = Color(0xFFFFC107),
                           modifier = Modifier.size(24.dp)
                       )
                       Spacer(modifier = Modifier.width(4.dp))
                       data.detail?.rating?.toString()?.let {
                           Text(
                               text = it,
                               fontWeight = FontWeight.Bold,
                               fontSize = 18.sp
                           )
                       }
                       Text(
                           text = "/5",
                           color = MaterialTheme.colorScheme.onSurfaceVariant,
                           fontSize = 14.sp,
                           modifier = Modifier.padding(start = 2.dp)
                       )
                   }
               }

               // Tab navigation
               var selectedTab by remember { mutableStateOf("Overview") }
               val tabs = listOf("Overview", "Details", "Reviews", "Location", "Weather")

               ScrollableTabRow(
                   selectedTabIndex = tabs.indexOf(selectedTab),
                   edgePadding = 0.dp,
                   backgroundColor = Color.Transparent,
                   contentColor = MaterialTheme.colorScheme.primary,
                   divider = {},
                   indicator = { tabPositions ->
                       Box(
                           modifier = Modifier
                               .tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)])
                               .height(3.dp)
                               .padding(horizontal = 16.dp)
                               .background(
                                   color = MaterialTheme.colorScheme.primary,
                                   shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                               )
                       )
                   }
               ) {
                   tabs.forEach { tab ->
                       Tab(
                           selected = selectedTab == tab,
                           onClick = { selectedTab = tab },
                           text = {
                               Text(
                                   text = tab,
                                   fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                                   color = if (selectedTab == tab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                               )
                           }
                       )
                   }
               }

               Spacer(modifier = Modifier.height(16.dp))

               // Content based on selected tab
               when (selectedTab) {
                   "Overview" -> data.detail?.let { OverviewContent(data = it, description = data.description, imgs = data.lstImgs) }
                   "Details" -> data.detail?.let { DetailsContent(data = it) }
                   "Reviews" -> data.detail?.let { ReviewsContent(data = it) }
                   "Location" -> data.detail?.let { LocationContent(data = it, location = data) }
                   "Weather" ->data.detail?.let { WeatherContent(data = it) }
               }
           }
       }

       Row(
           modifier = Modifier
               .padding(20.dp)
               .align(Alignment.BottomEnd)
           ,
           horizontalArrangement = Arrangement.spacedBy(20.dp), // Cách đều 20dp
           verticalAlignment = Alignment.CenterVertically,
       ) {
           BtnPrimary(
               onClick = {
                   navController.currentBackStackEntry?.savedStateHandle?.set("images", data.lstImgs)
                   navController.navigateWithAnimation(Screen.Gallery.route)
               },
               title = "Gallery",
               textColor = MaterialTheme.colorScheme.tertiary,
               backgroundColor = Color.White
           )

           IconBtn(
               icon = R.drawable.ar,
               onClick = {
                   navController.navigateWithAnimation(Screen.TourScreen.route)
               }
           )
       }
   }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OverviewContent(data: LocationDetailModel,description: String, imgs: List<String>) {
    val navController = LocalNavController.current
    Column {
        // Quick info card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Txt(
                    value = description,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Txt(
                    value = data.about,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Highlights section
        Text(
            text = "Highlights",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            data.highLights.forEach { high ->
                HighlightItem(
                    icon = high.icon,
                    title = high.title,
                    subtitle = high.subTitle,
                 modifier = Modifier
                            .widthIn(min = (getScreenWidth() / 3).dp)
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gallery section
        GalleryPreview(
            images = imgs,
            onShowFullGallery = {
                navController.currentBackStackEntry?.savedStateHandle?.set("images", imgs)
                navController.navigateWithAnimation(Screen.Gallery.route)
            }

        )
        Spacer(modifier = Modifier.height(16.dp))

        // Description section
        Text(
            text = "About",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = data.about,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Activities section
        Text(
            text = "Popular Activities",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            data.activities.forEach { ac ->
                ActivityItem(
                    icon = ac.icon,
                    title = ac.title,
                    description = ac.description
                )

            }

        }

    }
}

@Composable
fun DetailsContent(data : LocationDetailModel) {
    Column {
        // Location details
        InfoSection(
            title = data.detail.title,
            content = data.detail.content
        )
    }
}

@Composable
fun ReviewsContent(data : LocationDetailModel) {
    Column {
        // Overall rating
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Txt(
                    value = data.rating.toString(),
                    fontWeight = FontWeight.Bold,
                )

                Row {
                    repeat(data.rating.toInt()) { index ->
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = if (index < 5) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Txt(
                    value = "Based on " + data.totalReview+ "reviews",
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sample reviews
        Txt(
            value = "Recent Reviews",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        data.reviews.forEach{rv ->
            ReviewItem(
                name = rv.name,
                rating = rv.rating,
                date = rv.date,
                comment = rv.comment
            )

        }

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationContent(data: LocationDetailModel, location: Location) {
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    val hasLocationPermission = locationPermissionState.status.isGranted

    val gson = Gson()
    val route: Route = gson.fromJson(exampleRouteData, Route::class.java)

    val steps = route.routes.first().legs.first().steps
    val polylinePoints = route.routes.first().overview_polyline.points
    val startLocation = route.routes.first().legs.first().start_location

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(startLocation.lat, startLocation.lng),
            14f
        )
    }
    Column {
        // Map preview (placeholder)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission
                )
            ) {
                Polyline(
                    points = decodePolyline(polylinePoints),
                    color = MaterialTheme.colorScheme.primary,
                    width = 20f
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Address and directions
        Text(
            text = "Address",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = location.address,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // How to get there
        Text(
            text = "How to Get There",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        data.transportations.forEach { tr ->
            TransportOption(
                icon = tr.icon,
                title = tr.title,
                description = tr.description
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Nearby attractions
        Text(
            text = "Nearby Attractions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        data.nearbyAttractions.forEach { near ->
            NearbyAttraction(
                name = near.name,
                distance = near.distance.toString() + "KM",
                description = near.description
            )
        }

    }
}

@Composable
fun WeatherContent(data: LocationDetailModel) {
    Column {
        // Current weather
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Txt(
                    value = "Current Weather",
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.WbSunny,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Txt(
                            value = data.weather.current.wind,
                            fontWeight = FontWeight.Bold
                        )
                        Txt(
                            value = data.weather.current.condition,
                        )
                        Txt(
                            value = data.weather.current.uvIndex,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Txt(
                            value = data.weather.current.humidity,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }


                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weather forecast
        Title(
            value = "5-Day Forecast",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            for (forecastDay in data.weather.forecast) {
                item {
                    ForecastDay(day = forecastDay.day, icon = forecastDay.icon, high =forecastDay.high, low = forecastDay.low)
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Seasonal information
        Title(
            value = "Seasonal Information",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        data.weather.seasons.forEach { sea ->
            SeasonInfo(
                season = sea.season,
                description = sea.description,
                recommendation = sea.recommendation
            )
        }
    }
}

// Helper components

@Composable
fun HighlightItem(
    icon: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp).fillMaxSize()
    ) {
        Title(value = icon)
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ActivityItem(
    icon: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Title(value = icon)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InfoSection(
    title: String,
    content: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))
        Divider()
    }
}

@Composable
fun ReviewItem(
    name: String,
    rating: Float,
    date: String,
    comment: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Profile image placeholder
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = name.first().toString(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        repeat(5) { index ->
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = if (index < rating) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun TransportOption(
    icon: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Title(value =  icon)

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NearbyAttraction(
    name: String,
    distance: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Txt(
                    value = name,
                    fontWeight = FontWeight.Bold
                )
                Txt(
                    value = description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

           Chip(
                onClick = { /* Navigate to attraction details */ },
              colors = ChipDefaults.chipColors(
                  backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
             Txt(value = distance)

           }
        }
    }
}


@Composable
fun ForecastDay(
    day: String,
    icon: String,
    high: String,
    low: String
) {
    Card(
        modifier = Modifier.width(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp).widthIn(min = 300.dp)
        ) {
            Txt(
                value = day,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Title(value = icon)
            Spacer(modifier = Modifier.height(8.dp))

            Txt(
                value = high,
                fontWeight = FontWeight.Bold
            )

            Txt(
                value = low,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SeasonInfo(
    season: String,
    description: String,
    recommendation: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Txt(
                value = season,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Txt(
                value = description,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Recommend,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Txt(
                    value = recommendation,
                )
            }
        }
    }
}

