package com.example.hodos_final_android.service


import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationLocation
import com.example.hodos_final_android.model.PaginationLocationRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface LocationService {
    @GET("location/top-10")
    suspend fun locationTop10(): List<Location>

    @POST("location/pagination")
    suspend fun pagination(@Body() pagination: Pagination<PaginationLocation>): PaginationLocationRes
}

