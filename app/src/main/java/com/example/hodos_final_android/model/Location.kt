package com.example.hodos_final_android.model


@Suppress("PLUGIN_IS_NOT_ENABLED")
data class Location(
    val id: String,
    val createdAt: String,
    val createdBy: String?,
    val createdByName: String?,
    val updatedAt: String,
    val updatedBy: String?,
    val deleteBy: String?,
    val isDeleted: Boolean,
    val name: String,
    val address: String,
    val description: String,
    val label: String,
    val lstImgs: List<String>,
    val coordinates: String,
    val type: String,
    val img: String,
    var value: Any? = null  // Dùng `Any?` nếu muốn linh hoạt, hoặc kiểu dữ liệu cụ thể
)
