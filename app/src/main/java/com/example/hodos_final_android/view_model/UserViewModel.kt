package com.example.hodos_final_android.view_model

import Resource
import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hodos_final_android.helper.TokenManager
import com.example.hodos_final_android.helper.toMultipartBodyPart
import com.example.hodos_final_android.model.AuthData
import com.example.hodos_final_android.model.PricingPlanModel
import com.example.hodos_final_android.model.UserModel
import com.example.hodos_final_android.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

data class ProfileUpdateUiState(
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val avatarUri: Uri? = null,
    val avatar: String? = null
)

@Singleton
class UserViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthData?>(null)
    val authState: StateFlow<AuthData?> = _authState.asStateFlow()

    private val _uiState = MutableStateFlow(ProfileUpdateUiState())
    val uiState: StateFlow<ProfileUpdateUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors.asStateFlow()

    fun initUiState() {
        val user = _authState.value?.user
        val userDetail = user?.userDetail

        user?.let {
            _uiState.value = _uiState.value.copy(
                email = it.email ?: "",
                avatar = it.avatar
            )
        }

        userDetail?.let {
            _uiState.value = _uiState.value.copy(
                fullName = it.fullName ?: "",
                gender = it.gender ?: "",
                birthDate = it.birthDate ?: "",
                phoneNumber = it.phoneNumber ?: ""
            )
        }

        validateFields()
    }

    fun updateField(field: String, value: Any) {
        _uiState.value = when (field) {
            "email" -> _uiState.value.copy(email = value as String)
            "fullName" -> _uiState.value.copy(fullName = value as String)
            "phoneNumber" -> _uiState.value.copy(phoneNumber = value as String)
            "birthDate" -> _uiState.value.copy(birthDate = value as String)
            "gender" -> _uiState.value.copy(gender = value as String)
            "avatarUri" -> _uiState.value.copy(avatarUri = value as Uri?)
            else -> _uiState.value
        }

        validateFields()
    }

    private fun validateFields() {
        val errors = mutableMapOf<String, String>()
        val state = _uiState.value

        if (state.fullName.isBlank()) {
            errors["fullName"] = "Full name is required"
        }

        if (state.phoneNumber.isBlank()) {
            errors["phoneNumber"] = "Phone number is required"
        } else if (!Patterns.PHONE.matcher(state.phoneNumber).matches()) {
            errors["phoneNumber"] = "Invalid phone number format"
        }

        _validationErrors.value = errors
    }

    fun saveProfile(
        context: Context,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            validateFields()
            if (_validationErrors.value.isNotEmpty()) return@launch
            _isLoading.value = true

            try {
                val state = _uiState.value

                // Convert avatar Uri to MultipartBody.Part (nếu có)
                val avatarPart = state.avatarUri?.toMultipartBodyPart(context)

                // Tạo các RequestBody cho các field dạng text
                val fullName = state.fullName.toRequestBody("text/plain".toMediaTypeOrNull())
                val phoneNumber = state.phoneNumber.toRequestBody("text/plain".toMediaTypeOrNull())
                val birthDate = state.birthDate.toRequestBody("text/plain".toMediaTypeOrNull())
                val gender = state.gender.toRequestBody("text/plain".toMediaTypeOrNull())

                // Gửi request đến AuthRepository
                authRepository.updateUserProfile(
                    fullName = fullName,
                    phone = phoneNumber,
                    birthDate = birthDate,
                    gender = gender,
                    avatar = avatarPart
                ).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                updateAuthData(it)
                            }
                            onSuccess()
                        }

                        is Resource.Error -> {
                            onError(Exception("Unknown error"))
                            Toast.makeText(context,"" , Toast.LENGTH_SHORT).show()
                        }

                        is Resource.Loading -> {
                        }
                        else -> {

                        }
                    }
                }
            } catch (e: Exception) {
                onError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAuthData(authData: AuthData) {
        _authState.value = authData

        TokenManager.getInstance().saveTokens(
            refreshToken = authData.refreshToken,
            accessToken = authData.accessToken
        )
    }

    fun getUser(): UserModel? = _authState.value?.user

    fun getSuggestPricingPlan(): PricingPlanModel? = getUser()?.pricingPlanSuggest

    fun getAccessToken(): String? = _authState.value?.accessToken

    fun logout() {
        _authState.value = null
        TokenManager.getInstance().clearTokens()
    }
}
