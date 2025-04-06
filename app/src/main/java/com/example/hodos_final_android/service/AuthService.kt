package com.example.hodos_final_android.service


import com.example.hodos_final_android.model.AuthData
import com.example.hodos_final_android.model.GetUserInfoModel
import com.example.hodos_final_android.model.LoginModel
import com.example.hodos_final_android.model.RegisterModel
import com.example.hodos_final_android.model.ResendCodeModel
import com.example.hodos_final_android.model.Response
import com.example.hodos_final_android.model.VerifyModel
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit API service interface for network requests
 */
interface AuthService {
    @POST("auth/sign-up")
    suspend fun signUp(@Body() body: RegisterModel): Response


    @POST("auth/sign-in")
    suspend fun login(@Body() body: LoginModel): AuthData

    @POST("auth/resend-verification-code")
    suspend fun resendVerCode(@Body() body: ResendCodeModel): Response

    @POST("auth/verify")
    suspend fun verify(@Body() body: VerifyModel): Response

    @POST("user/detail")
    suspend fun userDetail(@Body() body: GetUserInfoModel): AuthData
}

