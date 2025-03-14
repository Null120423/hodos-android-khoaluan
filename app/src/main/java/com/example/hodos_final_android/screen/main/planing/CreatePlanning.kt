package com.example.hodos_final_android.screen.main.planing

import android.os.Build
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Work
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
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.ParentScreen
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.CalendarView
import com.example.hodos_final_android.navigateWithAnimation

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePlanning() {
    val navController = LocalNavController.current
    var currentStep by remember { mutableStateOf(0) }
    val totalSteps = 4

   ParentScreen {
       Box {
           Column(
               modifier = Modifier
                   .fillMaxSize()
                   .background(Color.White)
                   .padding(vertical = 100.dp, horizontal =  16.dp)

           ) {
               // Progress indicator
               LinearProgressIndicator(
                   progress = (currentStep + 1f) / totalSteps,
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(4.dp),
                   color = Color(0xFF2196F3),
                   trackColor = Color(0xFFE3F2FD)
               )

               Text(
                   "${currentStep + 1} of $totalSteps",
                   style = MaterialTheme.typography.labelSmall,
                   modifier = Modifier.padding(vertical = 8.dp)
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
                   modifier = Modifier.weight(1f)
               ) { step ->
                   when (step) {
                       0 -> TravelersQuestion()
                       1 -> DateSelectionQuestion()
                       2 -> BudgetQuestion()
                       3 -> PreferencesQuestion()
                   }
               }

               // Continue button
               Button(
                   onClick = {
                       if (currentStep < totalSteps - 1) {
                           currentStep++
                       }else {
                           navController.navigateWithAnimation(Screen.ReviewSummaryCreatePlanningScreen.route)
                       }


                   },
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(56.dp),
                   colors = ButtonDefaults.buttonColors(
                       containerColor = Color(0xFF2196F3)
                   )
               ) {
                   Text("CONTINUE")
               }
           }

           Box( modifier = Modifier.padding(20.dp)){
               IconButton(
                   onClick = {
                       if (currentStep > 0) {
                           currentStep--
                       } else {
                           navController.popBackStack()
                       }
                   },
                   modifier = Modifier
                       .align(Alignment.TopStart)
                       .padding(top = 8.dp)
                       .size(32.dp)
                       .clip(CircleShape)
                       .background(MaterialTheme.colorScheme.background)
               ) {
                   Icon(
                       imageVector = Icons.Default.ArrowBack,
                       contentDescription = "Back",
                       tint = MaterialTheme.colorScheme.tertiary
                   )
               }
           }

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSelectionQuestion() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "We will your adventure begin and end?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Choose the dates for your trip. This help us pls the perfect itinerary for you travel period",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        CalendarView(selectedDate = null, onDateSelected = {})
    }
}

@Composable
fun BudgetQuestion() {
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Set your trip budget",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Let us know your budget preferences and well craft an itinerary for your financial comfort",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val options = listOf(
            Triple("Cheap", Icons.Outlined.AttachMoney, "Traveling around just you"),
            Triple("Balanced", Icons.Outlined.Balance, "Perfect mix of comfort and value"),
            Triple("Luxury", Icons.Outlined.Star, "Premium comfort and experiences"),
            Triple("Flexible", Icons.Outlined.AllInclusive, "Mix of budget options")
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

@Composable
fun SelectableOption(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100.dp))
            .border(
                1.dp,
                if (isSelected) Color(0xFF2196F3) else Color.LightGray,
                RoundedCornerShape(100.dp)
            )
            .background(if (isSelected) Color(0xFFE3F2FD) else Color.White)
            .clickable(onClick = onSelect)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color(0xFF2196F3) else Color.Gray
        )

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
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
                if (isSelected) Color(0xFF2196F3) else Color.LightGray,
                RoundedCornerShape(100.dp)
            )
            .background(if (isSelected) Color(0xFFE3F2FD) else Color.White)
            .clickable(onClick = onToggle)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = Color(0xFF2196F3)
            )
        }
    }
}

