package com.example.hodos_final_android.screen.start

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.AnimateImg
import com.example.hodos_final_android.navigateWithAnimation

data class Question(
    val id: Int,
    val questionText: String,
    val options: List<String>
)

data class SubmitForm (
    val questionId: Int,
    val answerIndex: Int
)


val questions = listOf(
    Question(1, "What nation you want to visit?", listOf("France", "Japan", "Italy", "Canada", "Brazil")),
    Question(2, "Which language would you like to learn?", listOf("English", "Japanese", "French", "Spanish", "Italian")),
    Question(3, "What type of food do you prefer?", listOf("Pizza", "Sushi", "Pasta", "Burgers", "Tacos")),
    Question(4, "What did you ...?", listOf("1", "2", "3", "4", "5"))
)


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CollectInformationScreen() {
    var currentIndexQuestion by remember { mutableStateOf(0) }
    var currentQuestion by remember { mutableStateOf(questions[0]) }
    val options = currentQuestion.options
    val navController = LocalNavController.current

    val submitForm = remember { mutableStateListOf<SubmitForm>() }

    val isSelected = submitForm.any { it.questionId == currentQuestion.id }

    val handleNextQuestion = {
        if (isSelected) {
            if (currentIndexQuestion >= questions.size - 1) {
                navController.navigateWithAnimation(Screen.Main.route)
            } else {
                currentIndexQuestion++
                currentQuestion = questions[currentIndexQuestion]
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                LinearProgressIndicator(
                    progress = (currentIndexQuestion + 1) / questions.size.toFloat(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(4.dp),
                    color = Color(0xFF2196F3),
                    trackColor = Color(0xFFE0E0E0)
                )

                Text(
                    text = "${currentIndexQuestion + 1} of ${questions.size}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedContent(
                targetState = currentQuestion,
                transitionSpec = {
                    slideInHorizontally { width -> width } + fadeIn() with
                            slideOutHorizontally { width -> -width } + fadeOut()
                },
                label = "QuestionTransition"
            ) { question ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimateImg(
                            source = R.raw.collect_informatioon,
                            modifier = Modifier.size(250.dp)
                        )
                    }

                    Text(
                        text = question.questionText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    question.options.forEachIndexed { index, destination ->
                        val answerOfCurrentIndex = submitForm.find { it.questionId == question.id }?.answerIndex
                        val isSelect = index == answerOfCurrentIndex

                        DestinationOption(
                            text = destination,
                            isSelected = isSelect,
                            onClick = {
                                val existingAnswerIndex = submitForm.indexOfFirst { it.questionId == question.id }

                                if (existingAnswerIndex != -1) {
                                    submitForm[existingAnswerIndex] = SubmitForm(question.id, index)
                                } else {
                                    submitForm.add(SubmitForm(question.id, index))
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }


                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        enabled = isSelected,
                        onClick = handleNextQuestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = if (isSelected) {
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE0E0E0) // Màu xám khi chưa chọn
                            )
                        }
                    ) {
                        Text(
                            text = "Continue",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }



            }


        }
    }
}

@Composable
fun DestinationOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
    val borderColor = if (isSelected) Color(0xFF2196F3) else Color(0xFFE0E0E0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .border(1.dp, borderColor, RoundedCornerShape(28.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFF2196F3) else Color.White)
                .border(1.dp, if (isSelected) Color(0xFF2196F3) else Color(0xFFE0E0E0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
