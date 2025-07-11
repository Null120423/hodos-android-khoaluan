package com.example.hodos_final_android.screen.planing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.Title
import com.example.hodos_final_android.di.PlanTripModelEntryPoint
import com.example.hodos_final_android.replaceCurrentWithAnimation
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatingPlan() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val planTripViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, PlanTripModelEntryPoint::class.java)
            .planTripModel()
    }

    val planTripQuestionResultState by planTripViewModel.planTripResultState.collectAsState()

    // State management
    var isLoading by remember { mutableStateOf(true) }
    var loadingProgress by remember { mutableFloatStateOf(0f) }
    var currentStep by remember { mutableStateOf("Analyzing your preferences...") }

    // Loading steps
    val loadingSteps = listOf(
        "Analyzing your preferences...",
        "Finding best destinations...",
        "Creating personalized itinerary...",
        "Calculating optimal routes...",
        "Finalizing your trip plan..."
    )

    LaunchedEffect(planTripQuestionResultState.isLoading) {
        loadingSteps.forEachIndexed { index, step ->
            currentStep = step
            loadingProgress = (index + 1) / loadingSteps.size.toFloat()
            delay(1500)
        }
        if(!planTripQuestionResultState.isLoading && planTripQuestionResultState.data != null) {
            navController.replaceCurrentWithAnimation(Screen.CreatePlanningResultScreen.route)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (planTripQuestionResultState.error != null) {
            // Error State
            ErrorContent(
                errorMessage = planTripQuestionResultState.error!!.message,
                onRetry = {
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        } else {
            // Loading State
            LoadingContent(
                isLoading = isLoading,
                progress = loadingProgress,
                currentStep = currentStep
            )
        }
    }
}

@Composable
fun LoadingContent(
    isLoading: Boolean,
    progress: Float,
    currentStep: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Progress indicator
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )

                Text(
                    text = "${(progress * 100).toInt()}% Complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Main title
        Title(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            value = "Plan trip base your answer. Please wait in few seconds"
        )

        // Current step
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
            border = BorderStroke(1.dp, Color(0xFF0EA5E9).copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF0EA5E9), CircleShape)
                )

                Text(
                    text = currentStep,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF0C4A6E),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Tips card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
            border = BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(20.dp)
                )

                Column {
                    Text(
                        text = "Did you know?",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF92400E)
                    )
                    Text(
                        text = "We analyze thousands of travel data points to create the perfect itinerary just for you!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF92400E).copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorContent(
    errorMessage: String,
    onRetry: () -> Unit,
    onCancel: () -> Unit
) {
    val primaryColor =MaterialTheme.colorScheme.primary
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Error icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    Color(0xFFEF4444).copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(40.dp)
            )
        }

        // Error message
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Oops! Something went wrong",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444)
                )

                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Gray
                ),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Go Back")
            }
        }
    }
}



