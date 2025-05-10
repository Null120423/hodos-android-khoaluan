package com.example.hodos_final_android.screen.planing



import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.di.PlanTripModelEntryPoint
import com.example.hodos_final_android.di.UserViewModelEntryPoint
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.screen.planing.components.ActivityItem
import com.example.hodos_final_android.screen.planing.components.DaySelector
import com.example.hodos_final_android.screen.planing.components.TripHeader
import com.example.hodos_final_android.theme.greenColor
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlanningResultScreen() {
    val context = LocalContext.current
    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }

    val planTripViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, PlanTripModelEntryPoint::class.java)
            .planTripModel()
    }
    val planTripQuestionResultState by planTripViewModel.planTripResultState.collectAsState()
    val saveTripState by planTripViewModel.saveTripState.collectAsState()

    val trip = planTripQuestionResultState.data?.result
    var selectedDayIndex by remember { mutableIntStateOf(0) }
    val selectedDay = trip?.days?.get(selectedDayIndex)
    val navController = LocalNavController.current

    var saved  by remember { mutableIntStateOf(0) }

    val handleSaveTrip = {
        if(userViewModel.getAccessToken() == null) {
            navController.navigateWithAnimation(Screen.Login.route)
        }else {
            if (trip != null) {
                planTripViewModel.saveTrip(trip)
            }else {
                FancyToast.makeText(context, "Can not find trip data!", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show()
            }
        }
    }

    LaunchedEffect(saveTripState) {
        Log.i("API", saveTripState.data.toString())
        if(saveTripState.data != null && saved == 0) {
            FancyToast.makeText(context,
                saveTripState.data?.message ?: "", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()
            saved = 1
        }
        if(saveTripState.error != null && saved == 0) {
            FancyToast.makeText(context, saveTripState.error!!.message, FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show()
        }
        if(saveTripState.data != null  || saveTripState.error != null ){
            planTripViewModel.clearSaveTrip()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Main content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                if (trip != null) {
                    TripHeader(
                        trip = trip
                    )
                }
            }

            item {
                if (trip != null) {
                    trip.days?.let {
                        DaySelector(
                            days = it,
                            selectedDayIndex = selectedDayIndex,
                            onDaySelected = { selectedDayIndex = it }
                        )
                    }
                }
            }

            if (selectedDay != null) {
                itemsIndexed(selectedDay.activities.filterNotNull()) { index, activity ->
                    ActivityItem(
                        activity = activity,
                        index = index + 1
                    )
                }

            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                 },
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            IconButton(
                    onClick = { /* Open map view */ },
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f), CircleShape)
                ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Map View",
                    tint = Color.White
                )
            }
        }

        if (saved == 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            ) {
                IconButton(
                    enabled = !saveTripState.isLoading,
                    onClick = { handleSaveTrip() },
                    modifier = Modifier
                        .size(60.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "save",
                        tint = MaterialTheme.colorScheme.background,
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            ) {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(60.dp)
                        .background(greenColor, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Saved",
                        tint = MaterialTheme.colorScheme.background,
                    )
                }
            }

        }

        if (saveTripState.isLoading) {
            Loading(title = "")
        }

    }
}
