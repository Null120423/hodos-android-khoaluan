package com.hodos.hodos_final_android.service


import com.hodos.hodos_final_android.model.NotificationModel
import com.hodos.hodos_final_android.model.Pagination
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


data class NotificationPaginationResponse(
    val data: List<NotificationModel>,
    val total: Int,
    val unreadCount: Int,
    val skip: Int,
    val take: Int,
    val hasNext: Boolean,
    val nextSkip: Int
)

data class ReadNotificationResponse(
    val data: NotificationModel,
    val message: String
)

interface NotificationService {
    @POST("mobile/notification/pagination")
    suspend fun pagination(@Body() body: Pagination<Any>): NotificationPaginationResponse

    @GET("mobile/notification/{id}")
    suspend fun detail(@Path("id") id: String): NotificationModel

    @PUT("read/{id}")
    suspend fun read(@Path("id") id : String) : ReadNotificationResponse
}

