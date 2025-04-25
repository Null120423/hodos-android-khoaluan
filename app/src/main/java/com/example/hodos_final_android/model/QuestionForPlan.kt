package com.example.hodos_final_android.model



data class OptionForPlan(
    val label: String,
    val desc: String,
    val icon: String,
    val value: String
)

data class PlanTripQuestionResponse(
    val type: String,
    val question: String,
    val options: List<OptionForPlan>? = null
)
