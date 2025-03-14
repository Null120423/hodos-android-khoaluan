package com.example.hodos_final_android.screen.main.planing

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.hodos_final_android.LoadingViewModel
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.ParentScreen
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.CustomBottomSheet
import com.example.hodos_final_android.component.rememberBottomSheetController
import com.example.hodos_final_android.navigateWithAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Question(
    val type: String,
    val question: String,
    val singleValue: String? = null,
    val multiValues: List<String>? = null
)

val dataCollect = listOf(
    Question(
        type = "SELECT",
        question = "PARTY",
        singleValue = "A COUPLE"
    ),
    Question(
        type = "DATE",
        question = "TRIP DATES",
        singleValue = "20 / 02 / 2025"
    ),
    Question(
        type = "MULTI",
        question = "TYPE OF TRIP",
        multiValues = listOf("ADVENTURE", "CULTURE", "FOOD_DINNING")
    ),
    Question(
        type = "SELECT",
        question = "BUDGET",
        singleValue = "$1000 - $2000"
    )
)

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ReviewSummaryCreatePlanningScreen(
    viewModel: LoadingViewModel = viewModel()
) {
    val bottomSheetController = rememberBottomSheetController()
    val navController = LocalNavController.current
    val coroutineScope = rememberCoroutineScope()
    var currentQuestion by remember { mutableStateOf<Question?>(null) }

    val handleBuildTrip = {
        viewModel.showLoading()
        coroutineScope.launch {
            delay(5000)
            viewModel.hideLoading()
            navController.navigateWithAnimation(Screen.SuggestTrip.route)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("REVIEW SUMMARY") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        ParentScreen {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                // Destination Section
                ReviewSection(
                    title = "DESTINATION",
                    icon = Icons.Default.LocationOn,
                    onEditClick = {
                        // Handle destination edit
                        bottomSheetController.show()
                        currentQuestion = null
                    }
                ) {
                    Column {
                        GlideImage(
                            model = "https://plantotravel.vn/images/facebook_thumb_plantotravel.jpg",
                            contentDescription = "Vung Tau",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                        )
                        Text(
                            "VŨNG TÀU",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            "ĐỊA CHỈ :  My Chau Phu My Binh Dinh",
                        )
                    }
                }

                // Party Section
                ReviewSection(
                    title = dataCollect[0].question,
                    icon = Icons.Default.Group,
                    onEditClick = {
                        // Fixed: Removed the extra curly braces
                        bottomSheetController.show()
                        currentQuestion = dataCollect[0]
                    }
                ) {
                    dataCollect[0].singleValue?.let {
                        Text(it)
                    }
                }

                // Trip Dates Section
                ReviewSection(
                    title = dataCollect[1].question,
                    icon = Icons.Default.DateRange,
                    onEditClick = {
                        bottomSheetController.show()
                        currentQuestion = dataCollect[1]
                    }
                ) {
                    dataCollect[1].singleValue?.let {
                        Text(it)
                    }
                }

                // Type of Trip Section
                ReviewSection(
                    title = dataCollect[2].question,
                    icon = Icons.Default.Favorite,
                    onEditClick = {
                        bottomSheetController.show()
                        currentQuestion = dataCollect[2]
                    }
                ) {
                    InterestTagsSectionWithMaxItems(
                        multiValues = dataCollect[2].multiValues,
                    )
                }

                // Budget Section
                ReviewSection(
                    title = dataCollect[3].question,
                    icon = Icons.Default.AccountBalance,
                    onEditClick = {
                        bottomSheetController.show()
                        currentQuestion = dataCollect[3]
                    }
                ) {
                    dataCollect[3].singleValue?.let { InterestTag(it) }
                }

                // Build Journey Button
                Button(
                    onClick = {
                        handleBuildTrip()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text("BUILD MY JOURNEY")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            CustomBottomSheet(
                isVisible = bottomSheetController.isVisible(),
                onDismiss = { bottomSheetController.hide() },
                title = currentQuestion?.question ?: "Chỉnh sửa"
            ) {
                // Bottom sheet content based on currentQuestion
                currentQuestion?.let { question ->
                    when (question.type) {
                        "SELECT" -> {
                            TravelersQuestion()

                        }
                        "DATE" -> {
                            // Display date picker
                            DateSelectionQuestion()
                            // Add your date picker UI here
                        }
                        "MULTI" -> {
                            // Display multi-selection options
                            PreferencesQuestion()
                            // Add your multi-selection UI here
                        }
                    }
                } ?: run {
                    // Default content for destination editing
                    Text("Edit destination")
                    // Add your destination editing UI here
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestTagsSectionWithMaxItems(
    multiValues: List<String>?
) {
    // Using the proper FlowRow from Jetpack Compose
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Use the provided multiValues list if available
        if (!multiValues.isNullOrEmpty()) {
            multiValues.forEach { tag ->
                InterestTag(tag)
            }
        } else {
            // Fallback to default tags if multiValues is null or empty
            val defaultTags = listOf(
                "ADVENTURE", "CULTURAL", "RELAXATION", "FOOD & DINING",
                "OUTDOOR", "SHOPPING", "NIGHTLIFE", "HISTORICAL"
            )

            defaultTags.forEach { tag ->
                InterestTag(tag)
            }
        }
    }
}

@Composable
fun ReviewSection(
    title: String,
    icon: ImageVector,
    onEditClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
                Text(
                    title,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Edit",
                    tint = Color(0xFF2196F3)
                )
            }
        }
        content()
    }
}

@Composable
fun InterestTag(text: String) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        color = Color.White
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

// Removed the custom FlowRow implementation since we're using the one from Jetpack Compose