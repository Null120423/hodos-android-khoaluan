package com.example.hodos_final_android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.R
import com.example.hodos_final_android.helper.getScreenHeight

@Composable
fun EmptyView(
    title: String? = "No data ..."
) {
   Column(
       modifier = Modifier
           .padding(40.dp)
           .fillMaxSize()
           .wrapContentSize(Alignment.Center),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center
   ) {
       AnimateImg(
           source = R.raw.empty,
           modifier = Modifier.height((getScreenHeight()/2).dp)
       )
       Seprate(height = 20)
       if (title != null) {
           Title(
               fontWeight = FontWeight.Bold,
               value = title
           )
       }
   }
}


@Composable
fun EmptyStateWithIllustration() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        // Custom illustration using multiple icons
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(160.dp)
        ) {
            // Outer ring
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3).copy(alpha = 0.05f))
            )

            // Middle ring
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3).copy(alpha = 0.1f))
            )

            // Inner circle with icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "No notifications",
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF2196F3)
                )
            }

            // Floating elements for visual interest
            Box(
                modifier = Modifier
                    .offset(x = 50.dp, y = (-30).dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF9800).copy(alpha = 0.3f))
            )

            Box(
                modifier = Modifier
                    .offset(x = (-40).dp, y = 40.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50).copy(alpha = 0.3f))
            )

            Box(
                modifier = Modifier
                    .offset(x = 30.dp, y = 50.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE91E63).copy(alpha = 0.3f))
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Your inbox is empty",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "When you have new notifications, they'll show up here. Stay tuned!",
            fontSize = 16.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Quick actions
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilledTonalButton(
                onClick = { /* Handle refresh */ },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFF2196F3).copy(alpha = 0.1f),
                    contentColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Refresh")
            }

            FilledTonalButton(
                onClick = { /* Handle settings */ },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFFFF9800).copy(alpha = 0.1f),
                    contentColor = Color(0xFFFF9800)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Settings")
            }
        }
    }
}

@Composable
fun EmptyStateWithAnimation() {
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isAnimating = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
                .fillMaxSize()
        ) {
            // Animated bell icon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "No notifications",
                    modifier = Modifier.size(50.dp),
                    tint = Color(0xFF2196F3)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Nothing to see here",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your notifications will appear here",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )

        }
    }
}

@Composable
fun EmptyBlogWithAnimation() {
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isAnimating = true
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
                .fillMaxSize()
        ) {
            // Animated bell icon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Article,
                    contentDescription = "No blog posts",
                    modifier = Modifier.size(50.dp),
                    tint = Color(0xFF2196F3)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "0 result!",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )
        }
    }
}