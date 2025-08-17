package com.hodos.hodos_final_android.screen.planing

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import java.time.LocalDate

data class PlanEvent(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val dayNumber: Int,
    val isNew: Boolean = false,
    val isDeleting: Boolean = false
)

data class PlanDay(
    val date: LocalDate,
    val events: List<PlanEvent>
)

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlanning() {
}
