package com.example.hodos_final_android.model

data class Pagination<T>(
    val skip: Int,
    val take: Int,
    val where: T
)
