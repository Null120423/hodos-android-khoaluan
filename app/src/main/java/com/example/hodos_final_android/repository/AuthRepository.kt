package com.example.hodos_final_android.repository



import Resource
import com.example.hodos_final_android.model.AuthData
import com.example.hodos_final_android.model.GetUserInfoModel
import com.example.hodos_final_android.model.LoginModel
import com.example.hodos_final_android.model.RegisterModel
import com.example.hodos_final_android.model.ResendCodeModel
import com.example.hodos_final_android.model.Response
import com.example.hodos_final_android.model.VerifyModel
import com.example.hodos_final_android.service.AuthService
import com.example.hodos_final_android.service.api.safeApiCall
import kotlinx.coroutines.flow.Flow
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

    fun userInfo(body: GetUserInfoModel) :
            Flow<Resource<AuthData>> =  safeApiCall {
             service.userDetail(body)
    }
}


