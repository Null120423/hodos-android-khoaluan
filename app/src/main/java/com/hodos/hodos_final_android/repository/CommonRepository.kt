package com.hodos.hodos_final_android.repository



import Resource
import com.hodos.hodos_final_android.model.DashboardModel
import com.hodos.hodos_final_android.service.CommonService
import com.hodos.hodos_final_android.service.api.safeApiCall
import com.hodos.hodos_final_android.view_model.GetDashBoard
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommonRepository @Inject constructor(
    private val service: CommonService
) {
    fun dashboard(body: GetDashBoard): Flow<Resource<DashboardModel>> =
        safeApiCall { service.dashboard(body) }
}
