package com.hodos.hodos_final_android.screen.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hodos.hodos_final_android.LocalNavController
import com.hodos.hodos_final_android.Screen
import com.hodos.hodos_final_android.component.ImgWithUrl
import com.hodos.hodos_final_android.component.Seprate
import com.hodos.hodos_final_android.di.HomeViewModelEntryPoint
import com.hodos.hodos_final_android.di.UserViewModelEntryPoint
import com.hodos.hodos_final_android.helper.TokenManager
import com.hodos.hodos_final_android.model.BannerModel
import com.hodos.hodos_final_android.model.BlogModel
import com.hodos.hodos_final_android.model.GetUserInfoModel
import com.hodos.hodos_final_android.model.Location
import com.hodos.hodos_final_android.navigateWithAnimation
import com.hodos.hodos_final_android.view_model.AuthViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
    val navController = LocalNavController.current

    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }

    val homeViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, HomeViewModelEntryPoint::class.java)
            .homeViewModel()
    }

    val authState by userViewModel.authState.collectAsState()
    val homeState by homeViewModel.homeState.collectAsState()

    val blogs = homeState.data?.blogs

    // Configure status bar for dark theme
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    // Add pull-to-refresh state
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() =
        refreshScope.launch {
            refreshing = true
            homeViewModel.getDashboard(authState?.user?.id)
            delay(1500)
            refreshing = false
        }

    val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)

    LaunchedEffect(authState) {
        val accessToken = authState?.accessToken
        if (accessToken == null && homeState.isLoading) {
            val tokenManager = TokenManager.getInstance()
            val getUserInfoModel = tokenManager.getAccessToken()?.let {
                GetUserInfoModel(
                    accessToken = it,
                    refreshToken = tokenManager.getRefreshToken()!!
                )
            }
            if (getUserInfoModel != null) {
                authViewModel.userInfo(getUserInfoModel)
            }
        }
    }

    LaunchedEffect(homeState) {
        if(homeState.data == null && homeState.isLoading) {
            homeViewModel.getDashboard(authState?.user?.id)
            delay(1500)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                stickyHeader {
                    EnhancedHeader(
                        homeState.data?.unreadCount ?: 0
                    )
                    Seprate(height = 20)
                }

                item {
                    Column(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        // Featured Banner Section
                        if (homeState.isLoading) {
                            DarkSkeletonBanner()
                        } else {
                            EnhancedFeaturedBannerSection(
                                banners = if(homeState.data != null) homeState.data!!.banners else emptyList()
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        if (homeState.isLoading) {
                            DarkSkeletonContent()
                        } else {
                            Column {
                                // Main content sections with dark theme
                                DarkLocationSection(locations = homeState.data?.locationData?.lst ?: emptyList())
                                DarkFoodSection(locations = homeState.data?.foodData?.lst ?: emptyList())
                                if (blogs != null) {
                                    DarkNewsSection(news = blogs)
                                }
                                Spacer(modifier = Modifier.height(120.dp))
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}
@Composable
private fun EnhancedHeader(unreadCount: Int = 0) {
    val navController = LocalNavController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Enhanced Search Bar
            Surface(
                shape = RoundedCornerShape(28.dp),
                shadowElevation = 8.dp,
                modifier = Modifier.weight(1f)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable {
                            navController.navigateWithAnimation(Screen.SearchScreen.route)
                        },
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF718096)
                        )
                        Text(
                            text = "Search destinations, food...",
                            color = Color(0xFF718096),
                            fontSize = 16.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Enhanced Notification Button
            Box(
                contentAlignment = Alignment.TopEnd
            ) {
                Surface(
                    shape = CircleShape,
                    shadowElevation = 6.dp
                ) {
                    IconButton(
                        onClick = { navController.navigateWithAnimation(Screen.NotificationScreen.route) },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF667eea),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

               if(unreadCount > 0 && unreadCount != null) {
                   Box(
                       modifier = Modifier
                           .offset(x = (-4).dp, y = 4.dp)
                           .size(24.dp)
                           .background(Color.Red, shape = CircleShape),
                       contentAlignment = Alignment.Center
                   ) {
                       unreadCount.toString()                                                          .let {
                           Text(
                               text = it,
                               color = Color.White,
                               fontSize = 10.sp,
                               fontWeight = FontWeight.Bold
                           )
                       }
                   }
               }
            }

        }
    }
}

@Composable
private fun EnhancedFeaturedBannerSection(banners: List<BannerModel>) {
    if (banners.isNotEmpty()) {
        var currentIndex by remember { mutableStateOf(0) }
        val navController = LocalNavController.current

        // Auto-scroll effect
        LaunchedEffect(banners.size) {
            while (true) {
                delay(5000) // Change banner every 5 seconds
                currentIndex = (currentIndex + 1) % banners.size
            }
        }

        val currentBanner = banners[currentIndex]

        Surface(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {

                    }
            ) {
                // Banner background image with animation
                AnimatedContent(
                    targetState = currentBanner,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(400)) togetherWith
                                fadeOut(animationSpec = tween(400))
                    },
                    label = "banner_image"
                ) { banner ->
                    ImgWithUrl(
                        url = banner.thumbnail,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.2f),
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Black.copy(alpha = 0.8f)
                                )
                            )
                        )
                )

                // Content with animation
                AnimatedContent(
                    targetState = currentBanner,
                    transitionSpec = {
                        slideInVertically { it / 3 } + fadeIn() togetherWith
                                slideOutVertically { -it / 3 } + fadeOut()
                    },
                    label = "banner_content"
                ) { banner ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Featured",
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = banner.title,
                                fontSize = 18.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )

                            if (banner.description.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = banner.description,
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Animated dots indicator
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                banners.forEachIndexed { index, _ ->
                                    val isActive = index == currentIndex
                                    val dotWidth by animateFloatAsState(
                                        targetValue = if (isActive) 24f else 8f,
                                        animationSpec = tween(300),
                                        label = "dot_width"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .width(dotWidth.dp)
                                            .height(8.dp)
                                            .background(
                                                if (isActive) Color.White else Color.White.copy(alpha = 0.4f),
                                                RoundedCornerShape(4.dp)
                                            )
                                            .clickable { currentIndex = index }
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "View Banner",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable(onClick = {
                                        if(currentIndex < banners.size - 1 ) {
                                            currentIndex ++
                                        }else {
                                            currentIndex = 0
                                        }
                                    })
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun DarkLocationSection(locations: List<Location>) {
    DarkSectionWithCards(
        title = "Locations for you",
        emoji = "🏞️",
        items = locations,
        itemContent = { location ->
            EnhancedTravelCard(data = location)
        }
    )
}

@Composable
private fun DarkFoodSection(locations: List<Location>) {
    DarkSectionWithCards(
        title = "Foods for you",
        emoji = "🍜",
        items = locations,
        itemContent = { location ->
            EnhancedTravelCard(data = location)
        }
    )
}

@Composable
private fun DarkNewsSection(news: List<BlogModel>) {
    DarkSectionWithCards(
        title = "Helpful Posts",
        emoji = "📰",
        items = news,
        itemContent = { new ->
            DarkNewsCard(data = new)
        }
    )
}


@Composable
private fun <T> DarkSectionWithCards(
    title: String,
    emoji: String = "",
    items: List<T>,
    itemContent: @Composable (T) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Section header with emoji and arrow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (emoji.isNotEmpty()) {
                    Text(
                        text = emoji,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(items) { item ->
                itemContent(item)
            }
        }
    }
}


@Composable
private fun EnhancedTravelCard(data: Location) {
    var isPressed by remember { mutableStateOf(false) }
    val navController = LocalNavController.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "travel_card_scale"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp
    ) {
        Card(
            modifier = Modifier
                .width(200.dp)
                .height(140.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .clickable {
                    isPressed = true
                    navController.navigateWithAnimation(Screen.LocationDetailScreen.createRoute(data.id))
                },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                ImgWithUrl(
                    url = data.img,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = data.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2
                    )
                    data.address?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = Color.White.copy(0.7f),
                            maxLines = 2
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
private fun DarkNewsCard(data: BlogModel) {
    var isPressed by remember { mutableStateOf(false) }
    val navController = LocalNavController.current

    val handleDetail = {
        navController.currentBackStackEntry?.savedStateHandle?.set("url", "https://hodos-admin.gitlabserver.id.vn/blog-detail/"+data.id)
        navController.navigateWithAnimation(Screen.BlogDetailScreen.route)
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "dark_news_scale"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Gray.copy(0.1f),
        modifier = Modifier
            .width(280.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    isPressed = true
                    handleDetail()
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                // Image section
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(120.dp)
                ) {
                    Box {
                        ImgWithUrl(
                            url = data.thumbnail,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
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
                            text = data.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
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
private fun DarkSkeletonBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer(),
                color = Color.Gray.copy(0.5f)
            )
    )
}

@Composable
private fun DarkSkeletonContent() {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        repeat(3) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                        color =  Color.Gray.copy(0.5f)
                    )
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(3) {
                    Box(
                        modifier = Modifier
                            .size(width = 280.dp, height = 160.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                                color = Color.Gray.copy(0.5f)
                            )
                    )
                }
            }
        }
    }
}
