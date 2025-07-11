package com.example.hodos_final_android.service


import com.example.hodos_final_android.model.BlogModel
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface BlogService {
    @POST("mobile/blog/pagination")
    suspend fun pagination(@Body() pagination: Pagination<Any>): PaginationResponse<BlogModel>
}

