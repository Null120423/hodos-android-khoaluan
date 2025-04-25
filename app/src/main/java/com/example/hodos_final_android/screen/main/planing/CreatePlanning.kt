package com.example.hodos_final_android.screen.main.planing

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.CalendarView
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.Title
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.di.PlanTripModelEntryPoint
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.model.PlanTripQuestionResponse
import com.example.hodos_final_android.navigateWithAnimation
import dagger.hilt.android.EntryPointAccessors
import java.time.LocalDate


@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePlanning() {
    val context = LocalContext.current
    val planTripViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, PlanTripModelEntryPoint::class.java)
            .planTripModel()
    }
    val planTripQuestionState by planTripViewModel.planTripQuestionState.collectAsState()

    val navController = LocalNavController.current
    var currentStep by remember { mutableStateOf(0) }
    var totalSteps by remember { mutableStateOf(0) }
    var questionList by remember { mutableStateOf(emptyList<PlanTripQuestionResponse>()) }

    LaunchedEffect(Unit) {
        planTripViewModel.loadQuestionToCollect()
    }

    LaunchedEffect(planTripQuestionState) {
        val questions = planTripQuestionState.data
        if (!questions.isNullOrEmpty()) {
            // Fixed: Use size instead of length for collections in Kotlin
            totalSteps = questions.size + 1
            questionList = questions
            Log.i("API", questions.size.toString())
        }
    }

    MainLayout(
        content = {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(vertical = 100.dp)
                ) {
                    if (planTripQuestionState.isLoading) {
                        Loading()
                    }else {
                        LinearProgressIndicator(
                            progress = (currentStep + 1f) / totalSteps,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.secondary
                        )

                        Txt(
                            "${currentStep + 1} of $totalSteps",
                        )

                        // Question content with animation
                        AnimatedContent(
                            targetState = currentStep,
                            transitionSpec = {
                                // Slide in from right, slide out to left when going forward
                                if (targetState > initialState) {
                                    slideInHorizontally(
                                        animationSpec = tween(300),
                                        initialOffsetX = { fullWidth -> fullWidth }
                                    ) + fadeIn(animationSpec = tween(300)) togetherWith
                                            slideOutHorizontally(
                                                animationSpec = tween(300),
                                                targetOffsetX = { fullWidth -> -fullWidth }
                                            ) + fadeOut(animationSpec = tween(300))
                                } else {
                                    // Slide in from left, slide out to right when going back
                                    slideInHorizontally(
                                        animationSpec = tween(300),
                                        initialOffsetX = { fullWidth -> -fullWidth }
                                    ) + fadeIn(animationSpec = tween(300)) togetherWith
                                            slideOutHorizontally(
                                                animationSpec = tween(300),
                                                targetOffsetX = { fullWidth -> fullWidth }
                                            ) + fadeOut(animationSpec = tween(300))
                                }.using(SizeTransform(clip = false))
                            },
                            modifier = Modifier.weight(1f),
                            label = ""
                        ) { step ->
                            if (step < questionList.size) {
                                val question = questionList[step]
                                when (question.type) {
                                    "SINGLE_CHOICE" -> SingleChoiceQuestion(question)
                                    "MULTI_CHOICE" -> MultiChoiceQuestion(question)
                                    "DATE_RANGE" -> DateSelectionQuestion(question)
                                    else -> {
                                        // Optional: fallback case
                                        Text("Unknown question type: ${question.type}")
                                    }
                                }
                            } else {
                                // Review step
                                ReviewStep()
                            }

                        }

                        BtnPrimary(
                            minWidth = getScreenWidth() - 30,
                            title = "CONTINUE",
                            onClick = {
                                if (currentStep < totalSteps - 1) {
                                    currentStep++
                                } else {
                                    navController.navigateWithAnimation(Screen.ReviewSummaryCreatePlanningScreen.route)
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

// Added missing composable functions for handling different question types
@Composable
fun SingleChoiceQuestion(question: PlanTripQuestionResponse) {
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Txt(
            question.question,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier.padding(10.dp)
        ) {
            Txt(
                question.question,
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            question.options?.let {
                items(it.size) { index ->
                    val option = question.options[index]
                    // Using a simplified version since we don't have icons for dynamic options
                    SingleSelectOption(
                        title = option.icon + option.label,
                        isSelected = selectedOption == option.value,
                        onSelect = { selectedOption = option.value }
                    )
                }
            }
        }
    }
}

@Composable
fun MultiChoiceQuestion(question: PlanTripQuestionResponse) {
    val selectedOptions = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            question.question,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            question.options?.let {
                items(it.size) { index ->
                    val option = question.options[index]
                    MultiSelectOption(
                        title = option.icon + option.label,
                        isSelected = option.value in selectedOptions,
                        onToggle = {
                            if (option.value in selectedOptions) {
                                selectedOptions.remove(option.value)
                            } else {
                                selectedOptions.add(option.value)
                            }
                        }
                    )
                }
            }
        }
    }
}

// Added a simple review step
@Composable
fun ReviewStep() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Review Your Trip Plan",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            "You're all set! Review your selections and continue to see your personalized trip plan.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSelectionQuestion(
    question: PlanTripQuestionResponse
) {
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Title(
            value = question.question,
            fontWeight = FontWeight.Bold
        )

        Txt(
            "Choose the dates for your trip. This helps us plan the perfect itinerary for your travel period."
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalendarView(
            startDate = startDate,
            endDate = endDate,
            onDateSelected = { date ->
                if (startDate == null || (!(startDate == null || endDate == null))) {
                    startDate = date
                    endDate = null
                } else if (startDate != null && endDate == null) {
                    if (date.isBefore(startDate)) {
                        endDate = startDate
                        startDate = date
                    } else {
                        endDate = date
                    }
                }
            }
        )

        if (startDate != null && endDate != null) {
            Text(
                text = "Selected range: ${startDate.toString()} to ${endDate.toString()}",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun TravelersQuestion() {
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Who is going?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Let's get started by selecting who you're traveling with.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val options = listOf(
            Triple("Only me", Icons.Outlined.Person, "Traveling around just you"),
            Triple("A couple", Icons.Outlined.Favorite, "Traveling around just you two"),
            Triple("Family", Icons.Outlined.People, "Traveling around with family"),
            Triple("Friends", Icons.Outlined.Group, "Traveling around with friends"),
            Triple("Work", Icons.Outlined.Work, "Traveling around for business")
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            items(options.size) { index ->
                val (title, icon, subtitle) = options[index]
                SelectableOption(
                    title = title,
                    subtitle = subtitle,
                    icon = icon,
                    isSelected = selectedOption == title,
                    onSelect = { selectedOption = title }
                )
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreferencesQuestion() {
    val selectedOptions = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Tailor your adventure to your tastes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Let us know your preferences and we'll craft the perfect itinerary",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val options = listOf(
            "Cultural Experiences",
            "Outdoor Activities",
            "Food & Dining",
            "Relaxation",
            "Adventure Sports",
            "Cultural Experiences",
            "Outdoor Activities",
            "Food & Dining",
            "Relaxation",
            "Adventure Sports",
            "Cultural Experiences",
            "Outdoor Activities",
            "Food & Dining",
            "Relaxation",
            "Adventure Sports"
        )

        FlowRow(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            options.forEach { option ->
                MultiSelectOption(
                    title = option,
                    isSelected = option in selectedOptions,
                    onToggle = {
                        if (option in selectedOptions) {
                            selectedOptions.remove(option)
                        } else {
                            selectedOptions.add(option)
                        }
                    }
                )
            }
        }
    }
}

// Added a simpler version for dynamic options without icons
@Composable
fun SingleSelectOption(
    title: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100.dp))
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(100.dp)
            )
            .background(if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background)
            .clickable(onClick = onSelect)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Txt(
            title,
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun SelectableOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100.dp))
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                RoundedCornerShape(100.dp)
            )
            .background(if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background)
            .clickable(onClick = onSelect)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
        )

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Txt(
                title,
                fontWeight = FontWeight.Medium
            )
            Txt(
                subtitle,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun MultiSelectOption(
    title: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                RoundedCornerShape(100.dp)
            )
            .background(if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background)
            .clickable(onClick = onToggle)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Txt(
            title,
            fontWeight = FontWeight.Medium
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}