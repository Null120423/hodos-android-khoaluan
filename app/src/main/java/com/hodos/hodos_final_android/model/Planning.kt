package com.hodos.hodos_final_android.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

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
    val totalSave: Int? = 0,
    val tripDirection: TripDirection? = null
) : Serializable

data class TripDay(
    val id: String? = null,
    val dayNumber: Int,
    val date: String,
    val dayOfWeek: String,
    val activities: List<TripActivity>,
    val tripId: String? = null
) : Serializable

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
    val dayName: String? = null,
    val date: String? = null,
) : Serializable


data class SaveTripResponse(
    val message: String,
    val isSave: Boolean
)




data class TripDirection(
    val distance: Double,
    val duration: Double,
    val geometry: String,
    val weight: Double,
    val weight_name: String
) : Serializable

data class TripOverviewPolyline(
    val points: String
)



fun decodePolyline(encoded: String): List<LatLng> {
    val polyline = mutableListOf<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1f) shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1f) shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
        lng += dlng

        val latLng = LatLng(lat / 1E5, lng / 1E5)
        polyline.add(latLng)
    }

    return polyline
}


