package com.hodos.hodos_final_android.view_model

import Resource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hodos.hodos_final_android.model.PricingPlanModel
import com.hodos.hodos_final_android.repository.UserSubscriptionRepository
import com.hodos.hodos_final_android.service.PricingPlanSubDTO
import com.hodos.hodos_final_android.service.PricingPlanSubResponse
import com.hodos.hodos_final_android.service.UserHaveCompletedPaymentDTO
import com.hodos.hodos_final_android.service.api.ErrorRes
import com.hodos.hodos_final_android.service.api.parseJsonError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

data class PricingPlanState(
    val planActives: List<PricingPlanModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorRes? = null
)

data class UserSubscriptionState(
    val selectPlan: PricingPlanModel? = null
)

data class PricingPlanSubState(
    val res: PricingPlanSubResponse ? = null,
    val isLoading: Boolean = false,
    val error: ErrorRes? = null,
)

data class TransactionCheckState(
    val isCompleted: Boolean? = null,
    val message: String? = null,
    val isLoading: Boolean = false,
    val error: ErrorRes? = null,
)


@Singleton
class UserSubscriptionModel @Inject constructor(
    private val repository: UserSubscriptionRepository
) : ViewModel() {

    private val _activePlansState = MutableStateFlow(PricingPlanState())
    val activePlansState: StateFlow<PricingPlanState> = _activePlansState

    private val _pricingPlanSubState = MutableStateFlow(PricingPlanSubState())
    val pricingPlanSubState: StateFlow<PricingPlanSubState> = _pricingPlanSubState

    private val _state = MutableStateFlow(UserSubscriptionState())
    val state: StateFlow<UserSubscriptionState> = _state

    private val _transactionCheckState = MutableStateFlow(TransactionCheckState())
    val transactionCheckState: StateFlow<TransactionCheckState> = _transactionCheckState

    fun clearTransactionCheck(){
        _transactionCheckState.value = _transactionCheckState.value.copy(
            isCompleted = null,
            message = null
        )
    }

    fun transactionCheck() {
        if(pricingPlanSubState.value.res == null) {
            return
        }
        val body = UserHaveCompletedPaymentDTO(
            transactionId = pricingPlanSubState.value.res!!.result.metaData.id
        )
        repository.userHaveCompletedPayment(body)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if(result.data != null ) {
                            _transactionCheckState.value = _transactionCheckState.value.copy(
                                isCompleted = result.data.isCompleted,
                                message = result.data.message
                            )
                        }
                    }
                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        Log.i("API", result.message ?: "Unknown error")
                        _transactionCheckState.value = _transactionCheckState.value.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                    is Resource.Loading -> {
                        _transactionCheckState.value = _transactionCheckState.value.copy(
                            isLoading = true
                        )
                    }

                    else -> {
                        _pricingPlanSubState.value = _pricingPlanSubState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }
            }
            .launchIn(viewModelScope)

    }
    fun pricingPlanSub(){
        if(_state.value.selectPlan == null) {
            return
        }
        val bodySub = PricingPlanSubDTO(
            pricingPlanId = _state.value.selectPlan!!.id
        )

        repository.pricingPlanSub(bodySub)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _pricingPlanSubState.value = _pricingPlanSubState.value.copy(
                            res = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        Log.i("API", result.message ?: "Unknown error")
                        _pricingPlanSubState.value = _pricingPlanSubState.value.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                    is Resource.Loading -> {
                        _pricingPlanSubState.value = _pricingPlanSubState.value.copy(
                            isLoading = true
                        )
                    }

                    else -> {
                        _pricingPlanSubState.value = _pricingPlanSubState.value.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
            .launchIn(viewModelScope)

    }

    fun selectPlan(planSelect : PricingPlanModel) {
        _state.value = _state.value.copy(
            selectPlan = planSelect
        )


    }

    fun getActivePlans() {
        repository.getActivesPlan()
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _activePlansState.value = _activePlansState.value.copy(
                            planActives = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        Log.i("API", result.message ?: "Unknown error")
                        _activePlansState.value = _activePlansState.value.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                    is Resource.Loading -> {
                        _activePlansState.value = _activePlansState.value.copy(
                            isLoading = true
                        )
                    }

                    else -> {
                        _activePlansState.value = _activePlansState.value.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
