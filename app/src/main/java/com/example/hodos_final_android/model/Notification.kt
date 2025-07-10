package com.example.hodos_final_android.model

data class NotificationTypeData(
    val color: String,
    val name: String,
    val description: String
)
data class NotificationModel(
    val id: String,
    val createdAt: String,
    val createdBy: String?,
    val createdByName: String?,
    val updatedAt: String,
    val updatedBy: String?,
    val deleteBy: String?,
    val isDeleted: Boolean,
    val userId: String,
    val scheduledNotificationId: String?,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val readAt: String?,
    val type: String,
    val sentAt: String,
    val linkTo: String?,
    val metadata: NotificationMetadata?,
    val typeData: NotificationTypeData,
    val user: UserModel?,
    val username: String?,
    val avatar: String?
)
data class NotificationMetadata(
    val pricingPlan: PricingPlanModel?,
    val transaction: TransactionModel?,
    val userSubscription: UserSubscriptionModel?
)
