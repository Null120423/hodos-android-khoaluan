package com.example.hodos_final_android.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.model.New
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.theme.HodosTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewItem(
    data : New
) {
    val navController = LocalNavController.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val handleDetail = {
        navController.currentBackStackEntry?.savedStateHandle?.set("url", data.url)
        navController.navigateWithAnimation(Screen.PostDetailScreen.route)
    }

    HodosTheme {
        Card(
            onClick = {
                handleDetail()
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(screenWidth/2)
                .padding(0.dp)
                .clickable {
                },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ImgWithUrl(
                    url = data.thumbnail,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                )

                Column(modifier = Modifier.padding(10.dp)) {
                    Txt(
                        value = data.title,
                        size = 14,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
