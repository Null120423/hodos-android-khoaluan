package com.example.hodos_final_android.model


@Suppress("PLUGIN_IS_NOT_ENABLED")
data class Location(
    val id: String,
    val createdAt: String,
    val createdBy: String?,
    val createdByName: String?,
    val updatedAt: String,
    val updatedBy: String?,
    val deleteBy: String?,
    val isDeleted: Boolean,
    val name: String,
    val address: String,
    val description: String,
    val label: String,
    val lstImgs: List<String>,
    val coordinates: String,
    val type: String,
    val img: String,
    var value: Any? = null,
    val detail: LocationDetailModel? = null
)

data class LocationDetailModel(
    val rating: Float,
    val totalReview: Int,
    val visitors: Int,
    val images: List<String>,
    val about: String,
    val highLights: List<HighLight>,
    val activities: List<Activity>,
    val detail: Detail,
    val reviews: List<Review>,
    val nearbyAttractions: List<NearbyAttraction>,
    val transportations: List<Transportation>,
    val weather : Weather
)

data class Weather(
    val current: CurrentWeather,
    val forecast: List<ForecastDay>,
    val seasons: List<Season>
)

data class CurrentWeather(
    val temperature: Int,
    val feelsLike: Int,
    val condition: String,
    val humidity: String,
    val wind: String,
    val uvIndex: String
)

data class ForecastDay(
    val day: String,
    val icon: String,
    val high: String,
    val low: String
)

data class Season(
    val season: String,
    val description: String,
    val recommendation: String
)


data class Transportation (
    val icon: String,
    val title: String,
    val description: String
)

data class NearbyAttraction(
    val name: String,
    val distance: Float,
    val description: String
)
data class Detail(
    val title: String,
    val content: String
)
data class Review(
    val name: String,
    val rating : Float,
    val date: String,
    val comment: String
)

data class Activity(
    val icon: String,
    val title: String,
    val description: String
)

data class HighLight (
    val icon: String,
    val title: String,
    val subTitle: String
)

data class PaginationLocation(
    val name: String,
    val type: String
)






