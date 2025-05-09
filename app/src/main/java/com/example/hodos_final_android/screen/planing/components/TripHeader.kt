package com.example.hodos_final_android.screen.planing.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.model.Trip

@Composable
fun TripHeader(
    trip: Trip,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {


        Seprate(height = 40)
        Txt(
            size = 24,
            value = trip.typeTrip.capitalize(),
            color = MaterialTheme.colorScheme.background,
        )

        Txt(
            value = trip.budget.capitalize(),
            color = MaterialTheme.colorScheme.background,
        )

        Txt(
            value = trip.startDate + " - " + trip.endDate,
            color = MaterialTheme.colorScheme.background,
        )

        Txt(
            value = "Totals days : " + trip.totalDays,
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
        )


        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAvatar(
                letter = "T",
                backgroundColor = Color(0xFF4CAF50),
                modifier = Modifier.size(32.dp)
            )

            Seprate(width = 10)

            ProfileAvatar(
                letter = "N",
                backgroundColor = Color(0xFFFFA000),
                modifier = Modifier
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun ProfileAvatar(
    letter: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
