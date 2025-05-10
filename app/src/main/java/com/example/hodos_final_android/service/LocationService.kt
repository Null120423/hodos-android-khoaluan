package com.example.hodos_final_android.service


import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationLocation
import com.example.hodos_final_android.model.PaginationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface LocationService {
    @GET("location/top-10")
    suspend fun locationTop10(): List<Location>

    @POST("location/pagination")
    suspend fun pagination(@Body() pagination: Pagination<PaginationLocation>): PaginationResponse<Location>

    @GET("location/find-by-label/{label}")
    suspend fun findByLabel(
        @Path("label") label: String,
    ): Location

    @GET("location/{id}")
    suspend fun detail(
        @Path("id") id: String,
    ): Location

}

