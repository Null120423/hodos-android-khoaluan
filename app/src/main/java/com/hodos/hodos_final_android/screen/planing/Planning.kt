package com.hodos.hodos_final_android.screen.planing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hodos.hodos_final_android.LocalNavController
import com.hodos.hodos_final_android.Screen
import com.hodos.hodos_final_android.component.BtnPrimary
import com.hodos.hodos_final_android.component.ImgWithUrl
import com.hodos.hodos_final_android.navigateWithAnimation
import com.hodos.hodos_final_android.theme.HodosTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

data class PlanningItem(
    val title: String,
    val price: String,
    val imgUrl: String,
    val duration: String = "",
    val location: String = "",
    val rating: Float = 0f,
    val isPopular: Boolean = false
)

@Composable
fun PlanningScreen() {
    val scrollState = rememberScrollState()
    val systemUiController = rememberSystemUiController()
    var isVisible by remember { mutableStateOf(false) }

    // Configure status bar for light theme
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
        systemUiController.setNavigationBarColor(
            color = Color.White,
            darkIcons = true
        )
        isVisible = true
    }

    // Sample planning data (keeping your original data structure)
    val planningItems = listOf(
        PlanningItem(
            title = "2 Day 1 night in Vung Tau City",
            price = "320$",
            imgUrl = "https://www.agoda.com/wp-content/uploads/2024/07/vung-tau-vietnam-featured.jpg",
            duration = "2D 1N",
            location = "Vung Tau",
            rating = 4.5f,
            isPopular = true
        ),
        PlanningItem(
            title = "3 day and 2 night at Da Lat City",
            price = "1000$",
            imgUrl = "https://www.agoda.com/wp-content/uploads/2024/07/vung-tau-vietnam-featured.jpg",
            duration = "3D 2N",
            location = "Da Lat",
            rating = 4.8f,
            isPopular = false
        ),
        PlanningItem(
            title = "Sai gon Camping",
            price = "100$",
            imgUrl = "https://www.agoda.com/wp-content/uploads/2024/07/vung-tau-vietnam-featured.jpg",
            duration = "1D",
            location = "Ho Chi Minh",
            rating = 4.2f,
            isPopular = false
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FAFC),
                        Color.White,
                        Color(0xFFF1F5F9)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Modern Header
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    animationSpec = tween(600, easing = FastOutSlowInEasing),
                    initialOffsetY = { -it }
                ) + fadeIn(animationSpec = tween(600))
            ) {
                ModernPlanningHeader()
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // AI Planning Section
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        animationSpec = tween(700, delayMillis = 200, easing = FastOutSlowInEasing),
                        initialOffsetY = { it / 3 }
                    ) + fadeIn(animationSpec = tween(700, delayMillis = 200))
                ) {
                    ModernAIPlanningSection()
                }

                Spacer(modifier = Modifier.height(32.dp))

                Spacer(modifier = Modifier.height(32.dp))

                // System Planning Section
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        animationSpec = tween(600, delayMillis = 600, easing = FastOutSlowInEasing),
                        initialOffsetY = { it / 4 }
                    ) + fadeIn(animationSpec = tween(600, delayMillis = 600))
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "System Planning",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )

                            TextButton(
                                onClick = { /* Handle see all */ }
                            ) {
                                Text(
                                    text = "See All",
                                    color = Color(0xFF667eea),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = "See all",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF667eea)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Planning Cards
                        planningItems.forEachIndexed { index, item ->
                            ModernPlanningCard(
                                item = item,
                                delay = index * 150
                            )
                            if (index < planningItems.size - 1) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
private fun ModernPlanningHeader() {
    val navController = LocalNavController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Travel Planning",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = "Plan your perfect journey",
                fontSize = 16.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ModernAIPlanningSection() {
    val navController = LocalNavController.current
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ai_section_scale"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 12.dp,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2)
                        )
                    )
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isPressed = true
                    navController.navigateWithAnimation(Screen.CreatePlanning.route)
                }
                .padding(24.dp)
        ) {
            // Background pattern
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent
                            ),
                            radius = 300f
                        )
                    )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = "AI",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Planning",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Create personalized travel plans with our intelligent AI assistant",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Create button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Create",
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF667eea)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Create Now",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF667eea)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
}



@Composable
private fun ModernPlanningCard(
    item: PlanningItem,
    delay: Int = 0
) {
    var isVisible by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        isVisible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "planning_card_scale"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            animationSpec = tween(500, easing = FastOutSlowInEasing),
            initialOffsetX = { it }
        ) + fadeIn(animationSpec = tween(500))
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isPressed = true
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Image section
                Box(
                    modifier = Modifier
                        .size(108.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    ImgWithUrl(
                        url = item.imgUrl,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Popular badge
                    if (item.isPopular) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFF6B35),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Popular",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Duration badge
                    if (item.duration.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.7f),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = item.duration,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Content section
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = item.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (item.location.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    modifier = Modifier.size(14.dp),
                                    tint = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = item.location,
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        if (item.rating > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Rating",
                                    modifier = Modifier.size(14.dp),
                                    tint = Color(0xFFFBBF24)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = item.rating.toString(),
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.price,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF667eea)
                        )

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF667eea).copy(alpha = 0.1f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = "View details",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                tint = Color(0xFF667eea)
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
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
