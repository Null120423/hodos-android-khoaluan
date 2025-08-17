package com.hodos.hodos_final_android.service


import com.hodos.hodos_final_android.model.DashboardModel
import com.hodos.hodos_final_android.view_model.GetDashBoard
import retrofit2.http.Body
import retrofit2.http.POST


interface CommonService {
    @POST("common/dashboard")
    suspend fun dashboard(@Body()body: GetDashBoard): DashboardModel
}

