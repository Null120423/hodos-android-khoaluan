package com.hodos.hodos_final_android.model

data class Response(
    val message: String,
)


data class PaginationResponse<T>(
    val data: List<T>,
    val total: Int,
    val nextSkip: Int,
    val hasNext: Boolean,
    val take: Int
)
