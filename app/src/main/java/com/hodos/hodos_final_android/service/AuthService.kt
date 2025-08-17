package com.hodos.hodos_final_android.service


import com.hodos.hodos_final_android.model.AuthData
import com.hodos.hodos_final_android.model.GetUserInfoModel
import com.hodos.hodos_final_android.model.LoginModel
import com.hodos.hodos_final_android.model.RegisterModel
import com.hodos.hodos_final_android.model.ResendCodeModel
import com.hodos.hodos_final_android.model.Response
import com.hodos.hodos_final_android.model.VerifyModel
import com.hodos.hodos_final_android.view_model.LoginWithFacebookDto
import com.hodos.hodos_final_android.view_model.LoginWithGoogleDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

/**
 * Retrofit API service interface for network requests
 */
interface AuthService {
    @POST("auth/sign-up")
    suspend fun signUp(@Body() body: RegisterModel): Response


    @POST("auth/sign-in-mobile")
    suspend fun login(@Body() body: LoginModel): AuthData

    @POST("auth/sign-in-with-google")
    suspend fun loginWithGoogle(@Body() body: LoginWithGoogleDto): AuthData


    @POST("auth/sign-in-with-facebook")
    suspend fun loginWithFacebook(@Body() body: LoginWithFacebookDto): AuthData


    @POST("auth/resend-verification-code")
    suspend fun resendVerCode(@Body() body: ResendCodeModel): Response

    @POST("auth/verify")
    suspend fun verify(@Body() body: VerifyModel): Response

    @POST("user/detail")
    suspend fun userDetail(@Body() body: GetUserInfoModel): AuthData

    @Multipart
    @PUT("user")
    suspend fun updateUserProfile(
        @Part("fullName") fullName: RequestBody,
        @Part("phoneNumber") phone: RequestBody,
        @Part("birthDate") birthDate: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part avatar: MultipartBody.Part? = null
    ) : AuthData
}

