package com.example.hodos_final_android.repository



import Resource
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationResponse
import com.example.hodos_final_android.model.Post
import com.example.hodos_final_android.model.Response
import com.example.hodos_final_android.service.PostService
import com.example.hodos_final_android.service.api.safeApiCall
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val service: PostService
) {
    fun createPost(caption: RequestBody, imgs: List<MultipartBody.Part>?): Flow<Resource<Response>> =
        safeApiCall { service.createPost(caption, imgs) }

    fun pagination(body: Pagination<Any>) : Flow<Resource<PaginationResponse<Post>>> =
        safeApiCall {
            service.pagination(body)
        }

}

