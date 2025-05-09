package com.example.hodos_final_android.service


import com.example.hodos_final_android.model.PlanTripQuestionResponse
import com.example.hodos_final_android.model.PlanTripRes
import com.example.hodos_final_android.model.SaveTripResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface PlanTripService {
    @GET("plan-trip/load-question-to-collect")
    suspend fun loadQuestionToCollect(): List<PlanTripQuestionResponse>


    @POST("plan-trip/plan-trip")
    suspend fun planTrip(@Body()body: Any): PlanTripRes

    @POST("plan-trip/save-trip")
    suspend fun saveTrip(@Body()body: Any): SaveTripResponse

}

