package com.example.hodos_final_android.model

import java.util.Date


data class CreatePostRequest(
    val caption: String,
    val imageUrls: List<String>
)

data class Post(
    val title: String,
    val thumbnail: String,
    val imgs: List<String>,
    val tag: List<String>,
    val content: String,
    val userId: String,
    val username : String,
    val timePosted: Int,
    val commentCount: Int,
    val createdAt: Date,
    val locations: List<Location> = emptyList(),
    val user : UserModel,
    val status: String
)


