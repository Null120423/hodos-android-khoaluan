package com.example.hodos_final_android.screen.planing.components


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.model.TripActivity
import com.example.hodos_final_android.navigateWithAnimation


@Composable
fun ActivityItem(
    activity: TripActivity,
    modifier: Modifier = Modifier,
    index : Number
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        // Timeline
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 12.dp, top = 8.dp)
        ) {
            // Activity number indicator
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Txt(
                    value =""+ index + "",
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold
                )
            }

        }

        // Activity card
        Card(
            onClick = {
                navController.navigateWithAnimation(Screen.LocationDetailScreen.createRoute(activity.id))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Box{
                Column {
                    ImgWithUrl(
                        url = activity.img ?: "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    )

                    // Activity details
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        activity?.name?.let {
                            Txt(
                                value = it,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        activity?.address?.let {
                            Txt(
                                value = it,
                                color = MaterialTheme.colorScheme.tertiary,
                                maxLines = 2,
                            )
                        }

                        Txt(
                            value = "Time " + " : " + activity?.timeStart + " - " + activity?.timeEnd,
                            color = MaterialTheme.colorScheme.tertiary,
                            maxLines = 2,
                        )
                    }

                }
                Box(
                    modifier = Modifier.align(Alignment.TopEnd).padding(15.dp)
                ){
                    IconButton(
                        onClick = {
                            val gmmIntentUri = Uri.parse("geo:0,0?q=${activity.coordinates}(${activity.name})")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            }
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map View",
                            tint = Color.White
                        )
                    }

                }
            }

        }
    }
}
