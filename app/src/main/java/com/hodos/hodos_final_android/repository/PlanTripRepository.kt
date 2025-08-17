package com.hodos.hodos_final_android.repository



import Resource
import com.hodos.hodos_final_android.model.Pagination
import com.hodos.hodos_final_android.model.PaginationResponse
import com.hodos.hodos_final_android.model.PlanTripQuestionResponse
import com.hodos.hodos_final_android.model.PlanTripRes
import com.hodos.hodos_final_android.model.SaveTripResponse
import com.hodos.hodos_final_android.model.Trip
import com.hodos.hodos_final_android.model.TripDirection
import com.hodos.hodos_final_android.service.PlanTripService
import com.hodos.hodos_final_android.service.api.safeApiCall
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

    fun tripDirection(body: Any): Flow<Resource<TripDirection>> =
        safeApiCall { service.tripDirection(body) }

    fun paginationTripUser(body: Pagination<Any>): Flow<Resource<PaginationResponse<Trip>>> =
        safeApiCall { service.paginationTripUser(body) }



    fun detail(id: String): Flow<Resource<Trip>> =
        safeApiCall { service.detail(id) }


}
