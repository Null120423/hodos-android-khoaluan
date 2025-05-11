package com.example.hodos_final_android.model

// Sample data based on the provided JSON

data class PlanTripRes(
    val result: Trip,
    val message: String?
)

data class Trip(
    val totalDays: Int,
    val typeTrip: String,
    val startDate: String,
    val endDate: String,
    val budget: String,
    val favorites: List<String>? = emptyList(),
    val days: List<TripDay>? = emptyList(),
    val id: String? = null,
    val thumbnail: String? = null,
    val totalSave: Int? = 0
)

data class TripDay(
    val id: String? = null,
    val dayNumber: Int,
    val date: String,
    val dayOfWeek: String,
    val activities: List<TripActivity>,
    val tripId: String? = null
)

data class TripActivity(
    val id: String,
    val timeStart: String,
    val timeEnd: String,
    val totalTime: String,
    val name: String,
    val description: String,
    val address: String,
    val coordinates: String,
    val img: String,
    val locationId: String? = null,
    val tripDayId: String? = null,
)

data class SaveTripResponse(
    val message: String,
    val isSave: Boolean
)






