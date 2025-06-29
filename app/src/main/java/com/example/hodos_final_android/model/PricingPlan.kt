package com.example.hodos_final_android.model


data class PlanLimits(
    val maxTripsPerMonth: Int,
    val maxCollaboratorsPerTrip: Int
)

data class PricingPlanModel(
    val id: String,
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
    val limits: PlanLimits
)

