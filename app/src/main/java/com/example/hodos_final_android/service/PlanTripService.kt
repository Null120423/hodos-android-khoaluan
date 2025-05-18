package com.example.hodos_final_android.service


import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationResponse
import com.example.hodos_final_android.model.PlanTripQuestionResponse
import com.example.hodos_final_android.model.PlanTripRes
import com.example.hodos_final_android.model.SaveTripResponse
import com.example.hodos_final_android.model.Trip
import com.example.hodos_final_android.model.TripDirection
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface PlanTripService {
    @GET("plan-trip/load-question-to-collect")
    suspend fun loadQuestionToCollect(): List<PlanTripQuestionResponse>


    @POST("plan-trip/plan-trip")
    suspend fun planTrip(@Body()body: Any): PlanTripRes

    @POST("plan-trip/save-trip")
    suspend fun saveTrip(@Body()body: Any): SaveTripResponse

    @POST("plan-trip/pagination-trip-user")
    suspend fun paginationTripUser(@Body()pagination: Pagination<Any>): PaginationResponse<Trip>

    @GET("plan-trip/{id}")
    suspend fun detail(@Path("id") id: String): Trip

    @POST("plan-trip/trip-direction")
    suspend fun tripDirection(@Body()body: Any): TripDirection

}

