package com.example.hodos_final_android.repository



import Resource
import com.example.hodos_final_android.model.NotificationModel
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.service.NotificationPaginationResponse
import com.example.hodos_final_android.service.NotificationService
import com.example.hodos_final_android.service.ReadNotificationResponse
import com.example.hodos_final_android.service.api.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val service: NotificationService
) {

    fun pagination(body: Pagination<Any>): Flow<Resource<NotificationPaginationResponse>> =
        safeApiCall { service.pagination(body) }

    fun detail(id: String) : Flow<Resource<NotificationModel>> =
        safeApiCall { service.detail(id) }

    fun read(id: String) : Flow<Resource<ReadNotificationResponse>> =
        safeApiCall { service.read(id) }


}


