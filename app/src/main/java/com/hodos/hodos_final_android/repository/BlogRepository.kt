package com.hodos.hodos_final_android.repository



import Resource
import com.hodos.hodos_final_android.model.BlogModel
import com.hodos.hodos_final_android.model.Pagination
import com.hodos.hodos_final_android.model.PaginationResponse
import com.hodos.hodos_final_android.service.BlogService
import com.hodos.hodos_final_android.service.api.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BlogRepository @Inject constructor(
    private val service: BlogService
) {
    fun pagination(body: Pagination<Any>) : Flow<Resource<PaginationResponse<BlogModel>>> =
        safeApiCall {
            service.pagination(body)
        }
}

