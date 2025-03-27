package com.example.hodos_final_android.screen.main.planing

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.ParentScreen
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.CustomBottomSheet
import com.example.hodos_final_android.component.ImageCarousel
import com.example.hodos_final_android.component.VideoPlayerScreen
import com.example.hodos_final_android.component.rememberBottomSheetController
import com.example.hodos_final_android.navigateWithAnimation


@Preview()
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlanningDetail() {
    val navController = LocalNavController.current
    val scrollState = rememberScrollState()
    val imageUrls = listOf(
        "https://thumbs.dreamstime.com/z/plan-your-travel-infographic-guide-vacation-booking-concept-vector-illustration-flat-style-design-hotel-air-tickets-67156007.jpg",
        "https://plantotravel.vn/images/facebook_thumb_plantotravel.jpg",
        "https://hotelwoodlandnainital.com/wp-content/uploads/2024/09/Plan-Your-Trip.png"
    )
    val bottomSheetController = rememberBottomSheetController()
    ParentScreen {
        Box{

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Header with images
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(0.dp)
                ) {

                    ImageCarousel(
                        lstImgUrl = imageUrls,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        autoScrollDuration = 5000,
                        enableAutoScroll = true
                    )




                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )
                {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "2 Day 1 night in Vung Tau City",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth().padding(0.dp),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            // Image
                            GlideImage(
                                model = "https://plantotravel.vn/images/facebook_thumb_plantotravel.jpg",
                                contentDescription = "Video thumbnail for planning",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Play button overlay
                            IconButton(
                                onClick = { bottomSheetController.show() },
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .align(Alignment.Center)  // This aligns the button in the center of the Box
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play video",
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Title and location
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "DIA CHI",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    // Description Title
                    Text(
                        text = "Descriptions",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                    // Description Text
                    Text(
                        text = "Vung Tau là một thành phố ven biển nằm ở miền Nam, là một điểm đến lý tưởng cho những ai yêu thích thiên nhiên và muốn tìm kiếm những trải nghiệm mới.",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        lineHeight = 20.sp,
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Plan Header
                        Text(
                            text = "Plan detail",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )

                        // Days Text
                        Text(
                            text = "4 Days",
                            fontSize = 14.sp,
                            color = Color.Gray,
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Plan Items
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PlanItem(
                            number = "01",
                            title = "Spread Your Arms",
                            description = "To make this gesture feel more relaxed, stretch your arms so you start the movement. No bending of hands."
                        )

                        PlanItem(
                            number = "02",
                            title = "Rest at The Toe",
                            description = "The basis of this movement is jumping. Now, what needs to be considered is that you have to use the tips of your feet."
                        )

                        PlanItem(
                            number = "03",
                            title = "Adjust Foot Movement",
                            description = "Jumping Jack is not just an ordinary jump. But, you also have to pay close attention to leg movements."
                        )

                        PlanItem(
                            number = "04",
                            title = "Clapping Both Hands",
                            description = "This cannot be taken lightly. You see, without realizing it, the clapping of your hands helps you to keep your rhythm while doing the jumping jack."
                        )
                    }
                }

                CustomBottomSheet(
                    isVisible = bottomSheetController.isVisible(),
                    onDismiss = { bottomSheetController.hide() },
                    title = "Tiêu đề Bottom Sheet"
                ) {
                    VideoPlayerScreen(videoUrl = "https://firebasestorage.googleapis.com/v0/b/manager-project-3bc13.appspot.com/o/hodos-hack%2FScreenRecording_09-27-2024%2009-38-09_1.mov?alt=media&token=43dd82b2-811b-4eb9-bc28-9ef61b108565")
                }
            }

            Box(modifier = Modifier.padding(20.dp)) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(32.dp)
                        .clip(CircleShape).background(MaterialTheme.colorScheme.background)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }


            FloatingActionButton(
                onClick = {
                    navController.navigateWithAnimation(Screen.EditPlanning.route)
                },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                modifier = Modifier
                    .padding(30.dp)
                    .align(Alignment.BottomEnd),
                shape = RoundedCornerShape(100),
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Filled.BorderColor,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TravelImage(imgUrl: String, modifier: Modifier = Modifier) {
    GlideImage(
        model = imgUrl,
        contentDescription = imgUrl,
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun PlanItem(number: String, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Step number
        Text(
            text = number,
            color = Color(0xFF5C94D7), // Blue color for the number
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(48.dp)
        )

        // Blue dot indicator
        Box(
            modifier = Modifier
                .padding(top = 10.dp, end = 16.dp)
                .size(10.dp)
                .background(Color(0xFF5C94D7), CircleShape)
        )

        // Step content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = description,
                color = Color.Gray,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }
    }
}

