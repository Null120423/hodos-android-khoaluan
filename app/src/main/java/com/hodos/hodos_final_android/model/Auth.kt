package com.hodos.hodos_final_android.model


data class RegisterModel(
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

data class VerifyModel (
    val username: String,
    val email: String,
    val verifyCode: String
)

data class ResendCodeModel (
    val username: String,
    val email: String
)

data class LoginModel (
    val username: String,
    val password: String,
    val fcmToken : String?
)

data class GetUserInfoModel (
    val accessToken : String,
    val refreshToken : String
)