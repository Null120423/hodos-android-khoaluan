package com.example.hodos_final_android.service


import com.example.hodos_final_android.model.PlanTripQuestionResponse
import retrofit2.http.GET


interface PlanTripService {
    @GET("plan-trip/load-question-to-collect")
    suspend fun loadQuestionToCollect(): List<PlanTripQuestionResponse>

}

