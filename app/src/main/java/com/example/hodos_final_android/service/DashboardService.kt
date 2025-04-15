package com.example.hodos_final_android.service


import com.example.hodos_final_android.model.DashboardModel
import retrofit2.http.GET


interface CommonService {
    @GET("common/dashboard")
    suspend fun dashboard(): DashboardModel
}

