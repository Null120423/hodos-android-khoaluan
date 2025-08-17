package com.hodos.hodos_final_android.model

import java.util.Date

data class EnumData(
    val LOCATION_TYPE: LocationType
)

data class LocationType(
    val FOOD: String,
    val LOCATION: String
)

data class UserSubscriptionModel(
    val id: String,
    val createdAt: String,
    val createdBy: String?,
    val createdByName: String?,
    val updatedAt: String,
    val updatedBy: String?,
    val deleteBy: String?,
    val isDeleted: Boolean,
    val userId: String,
    val pricingPlanId: String,
    val startDate: String,
    val currentPeriodEndDate: String,
    val cancelledAt: String?,
    val status: String,
    val autoRenew: Boolean,
    val lastPaymentDate: String?,
    val nextPaymentDate: String?,
    val gatewaySubscriptionId: String?,
    val cancellationReason: String?,
    val isTrial: Boolean,
    val trialEndsAt: String?,
    val pricingPlan: PricingPlanModel
)


data class UserModel(
    val id: String,
    val isNeedVerify: Boolean,
    val createdAt: String?,
    val createdBy: String?,
    val createdByName: String?,
    val updatedAt: String?,
    val updatedBy: String?,
    val deleteBy: String?,
    val isDeleted: Boolean?,
    val username: String,
    val email: String,
    val avatar: String,
    val verifyAt: String?,
    val verifyCode: String?,
    val isActive: Boolean?,
    val verifyExpiredTime: String?,
    val isPremium: Boolean?,
    val tripsThisMonth: Int?,
    val collaboratorsUsed: Int?,
    val subscriptionEndDate: Date? = null,
    val isAutoRenew: Boolean = true,
    val subscriptionStatus: String? = "active",
    val pricingPlanSuggest: PricingPlanModel? = null,
    val userSubscription: UserSubscriptionModel? = null,
    val userDetail: UserDetail? = null,
)
data class AuthData(
    val accessToken: String,
    val refreshToken: String,
    val enumData: EnumData,
    val user: UserModel
)

data class UserDetail(
    val id: String = "",
    val userId: String = "",
    val fullName: String? = "",
    val phoneNumber: String? = "",
    val birthDate: String? = "",
    val gender: String? = "",
    val address: String = "",
    val nationality: String = "",
    val travelInterests: List<String> = emptyList(),
    val travelHistory: String = "",
    val languages: List<String> = emptyList(),
    val bio: String = "",
    val facebookUrl: String = "",
    val telegramUrl: String = "",
    val githubUrl: String = ""
)
