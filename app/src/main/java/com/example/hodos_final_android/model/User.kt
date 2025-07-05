package com.example.hodos_final_android.model

import java.util.Date

data class EnumData(
    val LOCATION_TYPE: LocationType
)

data class LocationType(
    val FOOD: String,
    val LOCATION: String
)

data class UserSubscription(
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


data class User(
    val id: String,
    val isNeedVerify: Boolean,
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
    val userDetail: Any?,
    val isPremium: Boolean,
    val tripsThisMonth: Int,
    val collaboratorsUsed: Int,
    val subscriptionEndDate: Date? = null,
    val isAutoRenew: Boolean = true,
    val subscriptionStatus: String = "active",
    val pricingPlanSuggest: PricingPlanModel? = null,
    val userSubscription: UserSubscription? = null
)
data class AuthData(
    val accessToken: String,
    val refreshToken: String,
    val enumData: EnumData,
    val user: User
)

