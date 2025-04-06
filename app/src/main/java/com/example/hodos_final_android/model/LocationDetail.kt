package com.example.hodos_final_android.model

import androidx.navigation.NavType
import androidx.navigation.navArgument

data class LocationDetail(
    val id: String,
    val name: String,
    val hotelName: String,
    val rating: Int,
    val reviews: Int,
    val region: String,
    val distanceToCenter: Int,
    val guests: Int,
    val stayDuration: Int,
    val amenities: List<String>,
    val pricePerNight: Int,
    val imageResId: Int
)



interface Destination {
    val route: String
    val title: String
}

object LocationListDestination : Destination {
    override val route = "location_list"
    override val title = "Location List"
}

object LocationDetailDestination : Destination {
    override val route = "location_detail"
    override val title = "Location Detail"
    const val locationId = "location_id"
    val arguments = listOf(navArgument(name = locationId) {
        type = NavType.StringType
    })
    fun createRouteWithParam(locationId: String) = "$route/${locationId}"
}

object AddLocationDestination : Destination {
    override val route = "add_location"
    override val title = "Add Location"
}


