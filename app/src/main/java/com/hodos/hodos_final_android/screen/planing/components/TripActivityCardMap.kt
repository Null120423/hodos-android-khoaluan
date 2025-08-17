package com.hodos.hodos_final_android.screen.planing.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hodos.hodos_final_android.component.ColumnStart
import com.hodos.hodos_final_android.component.ImgWithUrl
import com.hodos.hodos_final_android.component.RowBetween
import com.hodos.hodos_final_android.component.RowStart
import com.hodos.hodos_final_android.component.Seprate
import com.hodos.hodos_final_android.component.Txt
import com.hodos.hodos_final_android.model.TripActivity

@Composable
fun TripActivityCardMap(
    activity: TripActivity,
    modifier: Modifier = Modifier,
    onDirectionsClick: () -> Unit = {},
    index: Int
) {
    Card(
        modifier = modifier
            .fillMaxWidth().padding(10.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        ColumnStart(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {},
                modifier = Modifier.height(32.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                border = null
            ) {
                activity.date?.let {
                    Text(
                        text = activity.dayName + " " + activity.date,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Seprate(height = 10)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Day number circle
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Txt(
                        value = "$index",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        size = 12
                    )
                }

                // Activity image
                ImgWithUrl(
                    url = activity.img,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                // Textual info
                Column(
                ) {
                    Txt(
                        value = activity.name,
                        fontWeight = FontWeight.SemiBold,
                        size = 16,
                        maxLines = 1
                    )

                    Txt(
                        value = activity.address,
                        size = 14,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                    )
                }
            }

            Seprate(height = 10)

            RowBetween {
                RowStart {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Seprate(width = 4)
                    Txt(
                        value = "${activity.timeStart} - ${activity.timeEnd}",
                        size = 14,
                        fontWeight = FontWeight.Medium
                    )
                }
                Button(
                    onClick = onDirectionsClick,
                    modifier = Modifier.height(32.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    border = null
                ) {
                    Text(
                        text = "Directions",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

        }
    }
}

