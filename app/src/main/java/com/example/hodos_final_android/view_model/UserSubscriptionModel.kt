package com.example.hodos_final_android.view_model


import androidx.lifecycle.ViewModel
import com.example.hodos_final_android.model.PricingPlanModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class UserSubscriptionState(
    val planActives: List<PricingPlanModel> = emptyList()
)


@Singleton
class UserSubscriptionModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<UserSubscriptionState?>(null)
    val state: MutableStateFlow<UserSubscriptionState?> = _state

    fun getActivePlans(): List<PricingPlanModel> {
        return _state.value?.planActives ?: emptyList()
    }

}
