package com.example.hodos_final_android.model
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class EnumData(
    val LOCATION_TYPE: LocationType
)

data class LocationType(
    val FOOD: String,
    val LOCATION: String
)

data class User(
    val id: String,
    val createdAt: String,
    val createdBy: String?,
    val createdByName: String?,
    val updatedAt: String,
    val updatedBy: String?,
    val deleteBy: String?,
    val isDeleted: Boolean,
    val username: String,
    val email: String,
    val avatar: String,
    val verifyAt: String,
    val verifyCode: String,
    val isActive: Boolean,
    val verifyExpiredTime: String,
    val userDetail: Any?
)

data class AuthData(
    val accessToken: String,
    val refreshToken: String,
    val enumData: EnumData,
    val user: User
)


class UserViewModel : ViewModel() {
    private val _authData = MutableLiveData<AuthData>()
    val authData: LiveData<AuthData> get() = _authData

    fun updateAuthData(authData: AuthData) {
        _authData.value = authData
    }

    fun getAccessToken(): String? {
        return _authData.value?.accessToken
    }

    fun getRefreshToken(): String? {
        return _authData.value?.refreshToken
    }

}

