package com.example.hodos_final_android.screen.planing

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.CalendarView
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.Title
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.di.PlanTripModelEntryPoint
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.model.OptionForPlan
import com.example.hodos_final_android.model.PlanTripQuestionResponse
import com.example.hodos_final_android.navigateWithAnimation
import dagger.hilt.android.EntryPointAccessors
import java.time.LocalDate

data class Body(
    val answers: String
)

@SuppressLint("MutableCollectionMutableState")
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
    val planTripQuestionResultState by planTripViewModel.planTripResultState.collectAsState()

    val navController = LocalNavController.current
    var currentStep by remember { mutableStateOf(0) }
    var isAnswer by remember { mutableStateOf(false) }
    var totalSteps by remember { mutableStateOf(0) }
    var questionList by remember { mutableStateOf(emptyList<PlanTripQuestionResponse>()) }
    val answers = remember { mutableStateMapOf<Int, Any>() }

    LaunchedEffect(Unit) {
        planTripViewModel.loadQuestionToCollect()
    }

    LaunchedEffect(planTripQuestionState) {
        val questions = planTripQuestionState.data
        if (!questions.isNullOrEmpty()) {
            totalSteps = questions.size
            questionList = questions
            Log.i("API", questions.size.toString())
        }
    }


    LaunchedEffect(planTripQuestionResultState.data) {
        if(planTripQuestionResultState.data != null && isAnswer) {
            Log.i("API", "DONE")
            navController.popBackStack()
            navController.navigateWithAnimation(Screen.CreatePlanningResultScreen.route)
        }
    }

    val handlePlanTrip = {
          planTripViewModel.planTrip(Body(
              answers = answers.toString()
          ))
        isAnswer = true
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
                    }
                    else if(planTripQuestionResultState.isLoading){
                        Column {
                            Loading()
                            Seprate(height = 10)
                            Title(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                value = "Plan trip base your answer. Please wait in few seconds"
                            )
                        }
                    } else if(planTripQuestionResultState.error != null) {
                        Column {
                            Seprate(height = 10)
                            Title(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                value = planTripQuestionResultState.error!!.message
                            )
                        }
                    }
                    else {
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
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                                    exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                                ) {
                                    when (question.type) {
                                        "SINGLE_CHOICE" -> SingleChoiceQuestion(
                                            question = question,
                                            selectedAnswer = answers[step] as? OptionForPlan,
                                            onAnswerSelected = { answer ->
                                                answers[step] = answer
                                            }
                                        )
                                        "MULTI_CHOICE" -> MultiChoiceQuestion(
                                            question = question,
                                            selectedAnswers = answers[step] as? List<OptionForPlan> ?: emptyList(),
                                            onAnswersSelected = { selectedList ->
                                                answers[step] = selectedList
                                            }
                                        )
                                        "DATE_RANGE" -> DateSelectionQuestion(
                                            question = question,
                                            selectedDates = answers[step] as? Pair<LocalDate, LocalDate>,
                                            onDateRangeSelected = { dateRange ->
                                                answers[step] = dateRange
                                            }
                                        )
                                        else -> Text("Unknown question type: ${question.type}")
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
                                    handlePlanTrip()
                                }
                            },
                            disabled = !answers.containsKey(currentStep)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SingleChoiceQuestion(
    question: PlanTripQuestionResponse,
    selectedAnswer: OptionForPlan?,
    onAnswerSelected: (OptionForPlan) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(
            value = question.question,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Txt(
            "Select one option that best fits your preference."
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            question.options?.let {
                items(it.size) { index ->
                    val option = it[index]
                    SingleSelectOption(
                        title = option.icon + " "+ option.label,
                        isSelected = selectedAnswer?.value == option.value,
                        onSelect = {
                            onAnswerSelected(option)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MultiChoiceQuestion(
    question: PlanTripQuestionResponse,
    selectedAnswers: List<OptionForPlan>,
    onAnswersSelected: (List<OptionForPlan>) -> Unit
) {
    // Create a mutable state list to track selections
    val currentSelections = remember {
        mutableStateListOf<OptionForPlan>().apply {
            addAll(selectedAnswers)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Title(
            value = question.question,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Txt(
            "Select all options that apply to your preferences."
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            question.options?.let {
                items(it.size) { index ->
                    val option = it[index]
                    MultiSelectOption(
                        title = option.icon + ' '+ option.label,
                        isSelected = currentSelections.any { it.value == option.value },
                        onToggle = {
                            if (currentSelections.any { it.value == option.value }) {
                                currentSelections.removeAll { it.value == option.value }
                            } else {
                                currentSelections.add(option)
                            }
                            onAnswersSelected(currentSelections.toList())
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSelectionQuestion(
    question: PlanTripQuestionResponse,
    selectedDates: Pair<LocalDate?, LocalDate?>?,
    onDateRangeSelected: (Pair<LocalDate?, LocalDate?>) -> Unit
) {
    val startDate = selectedDates?.first
    val endDate = selectedDates?.second

    Column(modifier = Modifier.fillMaxWidth()) {
        Title(
            value = question.question,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Txt(
            "Choose the dates for your trip. This helps us plan the perfect itinerary for your travel period."
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalendarView(
            startDate = startDate,
            endDate = endDate,
            onDateSelected = { selectedDate ->
                when {
                    startDate == null || (startDate != null && endDate != null) -> {
                        // Chọn mới
                        onDateRangeSelected(selectedDate to null)
                    }
                    selectedDate < startDate -> {
                        // Đảo chiều nếu chọn nhỏ hơn start
                        onDateRangeSelected(selectedDate to startDate)
                    }
                    else -> {
                        onDateRangeSelected(startDate to selectedDate)
                    }
                }
            }
        )

        if (startDate != null && endDate != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Txt(
                value = "Selected range: ${startDate} to ${endDate}",
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun ReviewStep() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title(
            value = "Review Your Trip Plan",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Txt(
            "You're all set! Review your selections and continue to see your personalized trip plan.",
            textAlign = TextAlign.Center
        )
    }
}

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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Txt(
            title
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
fun MultiSelectOption(
    title: String,
    isSelected: Boolean,
    onToggle: () -> Unit
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