package com.example.hodos_final_android.model


data class Response<T>(
    val message: String,
    val data : T
)


data class PaginationResponse<T>(
    val data: List<T>,
    val total: Int,
    val nextSkip: Int,
    val hasNext: Boolean,
    val take: Int
)
