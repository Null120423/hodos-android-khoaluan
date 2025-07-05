package com.example.hodos_final_android.view_model


import androidx.lifecycle.ViewModel
import com.example.hodos_final_android.helper.TokenManager
import com.example.hodos_final_android.model.AuthData
import com.example.hodos_final_android.model.PricingPlanModel
import com.example.hodos_final_android.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserViewModel @Inject constructor() : ViewModel() {

    private val _authState = MutableStateFlow<AuthData?>(null)
    val authState: MutableStateFlow<AuthData?> = _authState

    fun updateAuthData(authData: AuthData) {
        _authState.value = AuthData(
            accessToken = authData.accessToken,
            refreshToken = authData.refreshToken,
            enumData = authData.enumData,
            user = authData.user
        )
        println("✅ Auth updated: $authData")
        println("✅ Acesstoken: ${authData.accessToken}")
        /// save token
        TokenManager.getInstance().saveTokens(
            refreshToken = authData.refreshToken,
            accessToken = authData.accessToken
        )

    }


    fun getUser() : User? {
        return _authState.value?.user
    }

    fun getSuggestPricingPlan() : PricingPlanModel? {
        return this.getUser()?.pricingPlanSuggest
    }
    fun getAccessToken(): String? {
        return _authState.value?.accessToken
    }

    fun logout() {
        _authState.value = null
        TokenManager.getInstance().clearTokens()
    }

}
