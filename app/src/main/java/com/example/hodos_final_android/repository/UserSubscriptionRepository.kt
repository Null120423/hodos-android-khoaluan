package com.example.hodos_final_android.repository



import Resource
import com.example.hodos_final_android.model.PricingPlanModel
import com.example.hodos_final_android.model.Response
import com.example.hodos_final_android.service.PricingPlanSubDTO
import com.example.hodos_final_android.service.PricingPlanSubResponse
import com.example.hodos_final_android.service.StartFreeTrialDTO
import com.example.hodos_final_android.service.UserHaveCompletedPaymentDTO
import com.example.hodos_final_android.service.UserHaveCompletedPaymentResponse
import com.example.hodos_final_android.service.UserSubscriptionService
import com.example.hodos_final_android.service.api.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserSubscriptionRepository @Inject constructor(
    private val service: UserSubscriptionService
) {
    fun getActivesPlan(): Flow<Resource<List<PricingPlanModel>>> =
        safeApiCall { service.getActivesPlan() }

    fun startFreeTrial(body: StartFreeTrialDTO) : Flow<Resource<Response>> =
        safeApiCall { service.startFreeTrial(body) }

    fun pricingPlanSub(body: PricingPlanSubDTO) : Flow<Resource<PricingPlanSubResponse>> =
        safeApiCall { service.pricingPlanSub(body) }

    fun userHaveCompletedPayment(body: UserHaveCompletedPaymentDTO) : Flow<Resource<UserHaveCompletedPaymentResponse>> =
        safeApiCall { service.userHaveCompletedPayment(body) }
}

