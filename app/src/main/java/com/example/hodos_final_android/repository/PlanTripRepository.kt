package com.example.hodos_final_android.repository



import Resource
import com.example.hodos_final_android.model.PlanTripQuestionResponse
import com.example.hodos_final_android.model.PlanTripRes
import com.example.hodos_final_android.model.SaveTripResponse
import com.example.hodos_final_android.service.PlanTripService
import com.example.hodos_final_android.service.api.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanTripRepository @Inject constructor(
    private val service: PlanTripService
) {
    fun loadQuestionToCollect(): Flow<Resource<List<PlanTripQuestionResponse>>> =
        safeApiCall { service.loadQuestionToCollect() }


    fun planTrip(body: Any): Flow<Resource<PlanTripRes>> =
        safeApiCall { service.planTrip(body) }


    fun saveTrip(body: Any): Flow<Resource<SaveTripResponse>> =
        safeApiCall { service.saveTrip(body) }


}
