package com.example.hodos_final_android.view_model

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hodos_final_android.helper.isValidEmail
import com.example.hodos_final_android.model.AuthData
import com.example.hodos_final_android.model.GetUserInfoModel
import com.example.hodos_final_android.model.LoginModel
import com.example.hodos_final_android.model.RegisterModel
import com.example.hodos_final_android.model.ResendCodeModel
import com.example.hodos_final_android.model.VerifyModel
import com.example.hodos_final_android.repository.AuthRepository
import com.example.hodos_final_android.service.api.ErrorRes
import com.example.hodos_final_android.service.api.parseJsonError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userViewModel: UserViewModel
) : ViewModel() {

    // State for sign up
    private val _signUpState = MutableStateFlow<ResponseState>(ResponseState())
    val signUpState: StateFlow<ResponseState> = _signUpState

    // State for verification
    private val _verifyState = MutableStateFlow<ResponseState>(ResponseState())
    val verifyState: StateFlow<ResponseState> = _verifyState

    // State for resend code
    private val _resendCodeState = MutableStateFlow<ResponseState>(ResponseState())
    val resendCodeState: StateFlow<ResponseState> = _resendCodeState

    // State for login
    private val _loginState = MutableStateFlow<ResponseDataState<AuthData>>(ResponseDataState())
    val loginState: StateFlow<ResponseDataState<AuthData>> = _loginState

    // State for user info == when exit app
    private val _userInfoSate = MutableStateFlow<ResponseDataState<AuthData>>(ResponseDataState(isLoading = true))
    val userInfoSate: StateFlow<ResponseDataState<AuthData>> = _userInfoSate

    fun signUp(email: String, password: String, username: String, confirmPassword: String) {
        val registerModel = RegisterModel(username, email, password, confirmPassword)

        if(!isValidEmail(email)) {
            val error = ErrorRes(
                message = "Email is not valid!"
            )
            _signUpState.value = ResponseState(error = error)

            return
        }
        if(!password.equals(confirmPassword)) {
            val error = ErrorRes(
                message = "Confirm password not match!"
            )

            _signUpState.value = ResponseState(error = error)

            return
        }


        Log.d("SignUp", "Calling API with: $registerModel") // Log dữ liệu gửi lên API

        authRepository.signUp(registerModel).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _signUpState.value = ResponseState(response = result.data)
                }
                is Resource.Error -> {
                    val error = result.message?.let { parseJsonError(it) }
                    _signUpState.value = ResponseState(error = error)
                }
                is Resource.Loading -> {
                    _signUpState.value = ResponseState(isLoading = true, error = _signUpState.value.error)
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    // Function to verify
    fun verify(verifyModel: VerifyModel) {


        if(verifyModel.verifyCode.length != 4 ){
            val error = ErrorRes(
                message = "Please fill full code!"
            )

            _verifyState.value = ResponseState(error = error)

            return
        }


        authRepository.verify(verifyModel).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _verifyState.value = ResponseState(response = result.data)
                }
                is Resource.Error -> {
                    val error = result.message?.let { parseJsonError(it) }
                    _verifyState.value = ResponseState(error = error)
                }
                is Resource.Loading -> {
                    _verifyState.value = ResponseState(isLoading = true)
                }

                else -> {
                    _verifyState.value = ResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    // Function to resend verification code
    fun resendVerificationCode(resendCodeModel: ResendCodeModel) {
        authRepository.resendVerifyCode(resendCodeModel).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _resendCodeState.value = ResponseState(response = result.data)
                }
                is Resource.Error -> {
                    val error = result.message?.let { parseJsonError(it) }
                    _resendCodeState.value = ResponseState(error = error)
                }
                is Resource.Loading -> {
                    _resendCodeState.value = ResponseState(isLoading = true)
                }

                else -> {
                    _resendCodeState.value = ResponseState()
                }
            }
        }.launchIn(viewModelScope)
    }


    // Function to resend verification code
    fun login(body: LoginModel) {
        authRepository.login(body).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { userViewModel.updateAuthData(it) }
                    _loginState.value = ResponseDataState(data = result.data)
                }
                is Resource.Error -> {
                    val error = result.message?.let { parseJsonError(it) }
                    _loginState.value = ResponseDataState(error = error)
                }
                is Resource.Loading -> {
                    _loginState.value = ResponseDataState(isLoading = true)
                }

                else -> {
                    _loginState.value = ResponseDataState()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun userInfo(body: GetUserInfoModel) {
        authRepository.userInfo(body).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        Log.i("API", "LAY THONG TIN USER THNAH CONG" + result.data)
                        userViewModel.updateAuthData(it)
                    }
                    _userInfoSate.value = ResponseDataState(data = result.data)
                }
                is Resource.Error -> {
                    result.message?.let { Log.i("API", it) }
                    val error = result.message?.let { parseJsonError(it) }
                    _userInfoSate.value = ResponseDataState(error = error)
                }
                is Resource.Loading -> {
                    _userInfoSate.value = ResponseDataState(isLoading = true)
                }

                else -> {
                    _userInfoSate.value = ResponseDataState()
                }
            }
        }.launchIn(viewModelScope)
    }
}
