package com.example.hodos_final_android.view_model

import com.example.hodos_final_android.model.Response
import com.example.hodos_final_android.service.api.ErrorRes

data class ResponseState(
    val error: ErrorRes? = null,
    val response: Response? = null,
    val isLoading: Boolean = false,
)

data class  ResponseDataState<T>(
    val error: ErrorRes? = null,
    val data: T? = null,
    val isLoading: Boolean = false,
)


