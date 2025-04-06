package com.example.hodos_final_android.service


import com.example.hodos_final_android.model.Location
import retrofit2.http.GET


interface LocationService {
    @GET("location/top-10")
    suspend fun locationTop10(): List<Location>
}

