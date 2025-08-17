package com.hodos.hodos_final_android.service


import com.hodos.hodos_final_android.model.PricingPlanModel
import com.hodos.hodos_final_android.model.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class StartFreeTrialDTO(
    val pricingPlanId: String
)

data class PricingPlanSubResponse(
    val result: Result,
    val receivingBank: ReceivingBank
)

data class Result(
    val message: String,
    val metaData: MetaData
)

data class MetaData(
    val id: String,
    val userId: String,
    val relatedEntityId: String,
    val relatedEntityType: String,
    val amount: String,
    val description: String,
    val status: String,
    val currency: String,
    val paymentGateway: String,
    val type: String,
    val gatewayTransactionId: String,
    val metadata: TransactionMetadata,
    val createdByName: String?,
    val createdBy: String?,
    val createdAt: String,
    val updatedAt: String,
    val isDeleted: Boolean
)

data class TransactionMetadata(
    val qrCode: String,
    val pricingPlan: PricingPlan
)

data class PricingPlan(
    val id: String,
    val createdAt: String,
    val createdBy: String?,
    val createdByName: String?,
    val updatedAt: String,
    val updatedBy: String?,
    val deleteBy: String?,
    val isDeleted: Boolean,
    val name: String,
    val planCode: String,
    val description: String,
    val price: String,
    val currency: String,
    val billingCycle: String,
    val features: List<String>,
    val isActive: Boolean,
    val trialPeriodDays: Int,
    val displayOrder: Int,
    val limits: Limits
)

data class Limits(
    val maxTripsPerMonth: Int,
    val maxCollaboratorsPerTrip: Int
)

data class ReceivingBank(
    val id: String,
    val createdAt: String,
    val createdBy: String?,
    val createdByName: String?,
    val updatedAt: String,
    val updatedBy: String?,
    val deleteBy: String?,
    val isDeleted: Boolean,
    val bankName: String,
    val bankCode: String,
    val accountNumber: String,
    val accountHolderName: String,
    val branchName: String,
    val currency: String,
    val isActive: Boolean
)

data class PricingPlanSubDTO (
    val pricingPlanId: String
)

data class UserHaveCompletedPaymentDTO(
    val transactionId: String
)
data class UserHaveCompletedPaymentResponse(
    val message : String,
    val isCompleted: Boolean
)

interface UserSubscriptionService {
    @GET("mobile/pricing-plan/active-plan")
    suspend fun getActivesPlan(): List<PricingPlanModel>

    @POST("mobile/user-subscription/start-free-trial")
    suspend fun startFreeTrial(@Body body: StartFreeTrialDTO): Response

    @POST("mobile/pricing-plan/sub")
    suspend fun pricingPlanSub(@Body body: PricingPlanSubDTO): PricingPlanSubResponse

    @POST("mobile/pricing-plan/check-transaction")
    suspend fun userHaveCompletedPayment(@Body body: UserHaveCompletedPaymentDTO) : UserHaveCompletedPaymentResponse
}

