package com.example.hodos_final_android.model

data class DashboardModel(
    val banners: List<String>,
    val foodData:FoodDataDashboard,
    val locationData: LocationDataDashboard
)

data class FoodDataDashboard(
    val lst: List<Location>,
    val total: Int
)
data class LocationDataDashboard(
    val lst: List<Location>,
    val total: Int
)