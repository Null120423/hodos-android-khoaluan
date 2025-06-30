package com.example.hodos_final_android.service


import com.example.hodos_final_android.model.PricingPlanModel
import com.example.hodos_final_android.model.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class StartFreeTrialDTO(
    val pricingPlanId: String
)


interface UserSubscriptionService {
    @GET("mobile/pricing-plan/active-plan")
    suspend fun getActivesPlan(): List<PricingPlanModel>

    @POST("mobile/user-subscription/start-free-tria")
    suspend fun startFreeTrial(@Body body: StartFreeTrialDTO): Response<Any>

}

