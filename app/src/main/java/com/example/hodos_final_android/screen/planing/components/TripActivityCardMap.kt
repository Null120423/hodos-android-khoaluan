package com.example.hodos_final_android.screen.planing.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.model.TripActivity

@Composable
fun TripActivityCardMap(
    activity: TripActivity,
    dayNumber: Int,
    modifier: Modifier = Modifier,
    onDirectionsClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {

        ImgWithUrl(
            url = activity.img,
            modifier = Modifier.height(100.dp).width(100.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Day number indicator with blue circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF4285F4), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$dayNumber",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Location name
                Text(
                    text = activity.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                // Time information
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFF4285F4),
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = "${activity.timeStart} - ${activity.timeEnd}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Directions button
                Button(
                    onClick = onDirectionsClick,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(150.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEEF3FF),
                        contentColor = Color(0xFF4285F4)
                    )
                ) {
                    Text(
                        text = "Directions",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
