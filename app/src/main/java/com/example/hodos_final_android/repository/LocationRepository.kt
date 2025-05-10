package com.example.hodos_final_android.repository



import Resource
import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationLocation
import com.example.hodos_final_android.model.PaginationResponse
import com.example.hodos_final_android.service.LocationService
import com.example.hodos_final_android.service.api.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val service: LocationService
) {
    fun locationTop10(): Flow<Resource<List<Location>>> =
        safeApiCall { service.locationTop10() }

    fun findByLabel(label: String) : Flow<Resource<Location>> =
        safeApiCall {
            service.findByLabel(label)
        }

    fun pagination(body: Pagination<PaginationLocation>) : Flow<Resource<PaginationResponse<Location>>> =
        safeApiCall {
            service.pagination(body)
        }

    fun detail(id: String) : Flow<Resource<Location>> =
        safeApiCall {
            service.detail(id)
        }
}
