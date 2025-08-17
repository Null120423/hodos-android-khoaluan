package com.hodos.hodos_final_android.repository



import Resource
import com.hodos.hodos_final_android.model.AuthData
import com.hodos.hodos_final_android.model.GetUserInfoModel
import com.hodos.hodos_final_android.model.LoginModel
import com.hodos.hodos_final_android.model.RegisterModel
import com.hodos.hodos_final_android.model.ResendCodeModel
import com.hodos.hodos_final_android.model.Response
import com.hodos.hodos_final_android.model.VerifyModel
import com.hodos.hodos_final_android.service.AuthService
import com.hodos.hodos_final_android.service.api.safeApiCall
import com.hodos.hodos_final_android.view_model.LoginWithFacebookDto
import com.hodos.hodos_final_android.view_model.LoginWithGoogleDto
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val service: AuthService
) {

    fun signUp(body: RegisterModel): Flow<Resource<Response>> =
        safeApiCall { service.signUp(body) }

    fun verify(body: VerifyModel): Flow<Resource<Response>> =
        safeApiCall { service.verify(body) }

    fun resendVerifyCode(body: ResendCodeModel):
            Flow<Resource<Response>> =
        safeApiCall { service.resendVerCode(body) }

    fun login(body: LoginModel) :
            Flow<Resource<AuthData>> =  safeApiCall {
                service.login(body)
    }
    fun loginWithGoogle(body: LoginWithGoogleDto):
            Flow<Resource<AuthData>> =  safeApiCall { service.loginWithGoogle(body) }

    fun loginWithFacebook(body: LoginWithFacebookDto):
            Flow<Resource<AuthData>> =  safeApiCall { service.loginWithFacebook(body) }

    fun userInfo(body: GetUserInfoModel) :
            Flow<Resource<AuthData>> =  safeApiCall {
             service.userDetail(body)
    }



    fun updateUserProfile(
        fullName: RequestBody,
        phone: RequestBody,
        birthDate: RequestBody,
        gender: RequestBody,
        avatar: MultipartBody.Part?
    ): Flow<Resource<AuthData>> = safeApiCall {
        service.updateUserProfile(fullName, phone, birthDate, gender, avatar)
    }

}


