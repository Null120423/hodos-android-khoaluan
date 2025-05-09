package com.example.hodos_final_android.model



data class OptionForPlan(
    val label: String,
    val desc: String,
    val icon: String,
    var value: String
)

data class PlanTripQuestionResponse(
    val type: String,
    val question: String,
    val options: List<OptionForPlan>? = null
)

data class PlanTripResult(
    val timeStart: String,
    val timeEnd: String,
    val location: PlanTripLocation?,
    val food: PlanTripFood?,
    val totalTime: String,
    val activities: List<String>,
    val transportation: String
)

data class PlanTripLocation(
    val id: String,
    val name: String,
    val images: List<String>,
    val address: String
)

data class PlanTripFood(
    val id: String,
    val name: String,
    val images: List<String>,
    val address: String
)
