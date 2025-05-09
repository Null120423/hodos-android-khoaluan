package com.example.hodos_final_android.screen.planing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.PlanningCard
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.theme.HodosTheme


@Composable
fun PlanningScreen() {
    val scrollState = rememberScrollState()

    MainLayout(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = 100.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    AIPlanningSection()

                    Spacer(modifier = Modifier.height(24.dp))

                    // System Planning Title
                    Text(
                        text = "System planning",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Planning Cards
                    PlanningCard(
                        title = "2 Day 1 night in Vung Tau City",
                        price = "320$",
                        imgUrl = "https://www.agoda.com/wp-content/uploads/2024/07/vung-tau-vietnam-featured.jpg"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PlanningCard(
                        title = "3 day and 2 night at Da Lat City",
                        price = "1000$",
                        imgUrl = "https://www.agoda.com/wp-content/uploads/2024/07/vung-tau-vietnam-featured.jpg"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PlanningCard(
                        title = "Sai gon Camping",
                        price = "100$",
                        imgUrl = "https://www.agoda.com/wp-content/uploads/2024/07/vung-tau-vietnam-featured.jpg"
                    )
                }
            }
        }
    )
}


@Composable
fun AIPlanningSection() {
    val navController = LocalNavController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE3F2FD))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Create new planning by AI",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.weight(0.5f)
            )
            BtnPrimary(
                title = "Create Now",
                onClick = { navController.navigateWithAnimation(Screen.CreatePlanning.route) },
                modifier = Modifier.weight(0.4f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlanningScreenPreview() {
    HodosTheme {
        PlanningScreen()
    }
}