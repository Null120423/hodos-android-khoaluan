package com.example.hodos_final_android.repository



import Resource
import com.example.hodos_final_android.model.DashboardModel
import com.example.hodos_final_android.service.CommonService
import com.example.hodos_final_android.service.api.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommonRepository @Inject constructor(
    private val service: CommonService
) {
    fun dashboard(): Flow<Resource<DashboardModel>> =
        safeApiCall { service.dashboard() }
}
