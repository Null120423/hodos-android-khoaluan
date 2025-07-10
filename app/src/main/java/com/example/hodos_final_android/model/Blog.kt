package com.example.hodos_final_android.model

data class BlogModel(
    val id: String,
    val createdAt: String,
    val createdBy: String?,
    val createdByName: String?,
    val updatedAt: String,
    val updatedBy: String?,
    val deleteBy: String?,
    val isDeleted: Boolean,
    val title: String,
    val thumbnail: String,
    val tag: String,
    val content: String? = null,
    val isPublish: Boolean
)
