package com.hodos.hodos_final_android.repository



import Resource
import com.hodos.hodos_final_android.model.PricingPlanModel
import com.hodos.hodos_final_android.model.Response
import com.hodos.hodos_final_android.service.PricingPlanSubDTO
import com.hodos.hodos_final_android.service.PricingPlanSubResponse
import com.hodos.hodos_final_android.service.StartFreeTrialDTO
import com.hodos.hodos_final_android.service.UserHaveCompletedPaymentDTO
import com.hodos.hodos_final_android.service.UserHaveCompletedPaymentResponse
import com.hodos.hodos_final_android.service.UserSubscriptionService
import com.hodos.hodos_final_android.service.api.safeApiCall
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

