package com.example.hodos_final_android.model
import java.util.Date

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
    val userDetail: Any?,
    val isPremium: Boolean,
    val tripsThisMonth: Int,
    val collaboratorsUsed: Int,
    val subscriptionEndDate: Date? = null,
    val isAutoRenew: Boolean = true,
    val subscriptionStatus: String = "active",
    val pricingPlanSuggest: PricingPlanModel? = null
)

data class AuthData(
    val accessToken: String,
    val refreshToken: String,
    val enumData: EnumData,
    val user: User
)


