package com.example.hodos_final_android.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.di.UserViewModelEntryPoint
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.navigateWithAnimation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.EntryPointAccessors

data class FeatureCard(
    val title: String,
    val subtitle: String = "",
    val description: String = "",
    val icon: ImageVector,
    val gradient: List<Color>,
    val accentColor: Color,
    val onClick: () -> Unit
)

data class SmallFeature(
    val title: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconColor: Color = Color(0xFF2D3748),
    val onClick: () -> Unit
)

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun FeatureDiscoveryScreen() {
    val navController = LocalNavController.current
    val systemUiController = rememberSystemUiController()
    var searchQuery by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    // Configure status bar
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = Color.White,
            darkIcons = true
        )
        isVisible = true
    }

    // Enhanced main feature cards
    val mainFeatures = listOf(
        FeatureCard(
            title = "AI Predict",
            subtitle = "Image Recognition",
            description = "Advanced AI-powered image analysis",
            icon = Icons.Default.CameraAlt,
            gradient = listOf(
                MaterialTheme.colorScheme.primary.copy(0.5f),
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            ),
            accentColor = Color(0xFF667eea),
            onClick = {
                navController.navigateWithAnimation(Screen.PredictScreen.route)
            }
        ),
        FeatureCard(
            title = "Smart Planning",
            subtitle = "AI Assistant",
            description = "Intelligent scheduling and organization",
            icon = Icons.Default.Schedule,
            gradient = listOf(
                Color(0xFF2ebf91),
                Color(0xFF2ebf91).copy(0.5f),
                Color(0xFF2ebf91).copy(0.7f),
            ),
            accentColor = Color(0xFF2ebf91),
            onClick = {
                navController.navigateWithAnimation(Screen.Planning.route)
            }
        ),
        FeatureCard(
            title = "ChatBot",
            subtitle = "AI Conversation",
            description = "Natural language AI assistant",
            icon = Icons.Default.Chat,
            gradient = listOf(
                Color(0xFFf093fb),
                Color(0xFFf5576c),
                Color(0xFFf093fb).copy(alpha = 0.8f)
            ),
            accentColor = Color(0xFFf5576c),
            onClick = {
                navController.navigateWithAnimation(Screen.ChatAiDashBoard.route)
            }
        ),
        FeatureCard(
            title = "AR Experience",
            subtitle = "Augmented Reality",
            description = "Immersive AR interactions",
            icon = Icons.Default.ViewInAr,
            gradient = listOf(
                Color(0xFF4facfe),
                Color(0xFF00f2fe),
                Color(0xFF4facfe).copy(alpha = 0.8f)
            ),
            accentColor = Color(0xFF00f2fe),
            onClick = {
                navController.navigateWithAnimation(Screen.ComingSoonScreen.route)
            }
        )
    )

    // Enhanced secondary features
    val secondaryFeatures = listOf(
        FeatureCard(
            title = "Locations & Foods",
            subtitle = "Explore Places & Local Dishes",
            description =  "Discover popular destinations and authentic cuisine from around the world",
            icon = Icons.Default.LocalDining,
            gradient = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary.copy(0.5f),
            ),
            accentColor = Color(0xFFfa709a),
            onClick = { navController.navigateWithAnimation(Screen.SearchScreen.route) }
        ),
        FeatureCard(
            title = "Helpful Post & Tips",
            subtitle = "Practical Guides & Insights",
            description = "Explore curated advice and expert tips to make the most of your experience",
            icon = Icons.Default.PostAdd,
            gradient = listOf(
                Color(0xFF667eea),
                Color(0xFF764ba2)
            ),
            accentColor = Color(0xFF667eea),
            onClick = { navController.navigateWithAnimation(Screen.BlogListScreen.route) }
        )
    )

    // Enhanced small features
    val smallFeatures = listOf(
        SmallFeature("Scanner", Icons.Default.QrCodeScanner, Color(0xFFE3F2FD), Color(0xFF1976D2)) { },
        SmallFeature("Translate", Icons.Default.Translate, Color(0xFFE8F5E8), Color(0xFF388E3C)) { },
        SmallFeature("History", Icons.Default.History, Color(0xFFFFF3E0), Color(0xFFF57C00)) { },
        SmallFeature("Settings", Icons.Default.Settings, Color(0xFFF3E5F5), Color(0xFF7B1FA2)) { },
        SmallFeature("Help", Icons.Default.Help, Color(0xFFE0F2F1), Color(0xFF00695C)) { },
        SmallFeature("Favorites", Icons.Default.Favorite, Color(0xFFFFEBEE), Color(0xFFC62828)) { }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
               MaterialTheme.colorScheme.background
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                EnhancedHeaderSection(
                    onProfileClick = { /* Navigate to profile */ },
                    onUpgradeClick = { /* Navigate to upgrade */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item{
                EnhancedSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
            }


            item {
                Text(
                    text = "AI-Powered Features",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyVerticalGrid(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    modifier = Modifier.height(270.dp),
                    columns = GridCells.Fixed(2),
                ) {
                    items(mainFeatures) { feature ->
                        EnhancedSecondaryFeatureCard(
                            feature = feature,
                            modifier = Modifier.width((getScreenWidth() / 2 - 10).dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item{
                Text(
                    text = "Additional",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4A5568),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    secondaryFeatures.forEach { feature ->
                        EnhancedSecondaryFeatureCard(
                            feature = feature,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }




            item {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Quick Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4A5568),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    modifier = Modifier.height(250.dp)
                ) {
                    items(smallFeatures) { feature ->
                        EnhancedSmallFeatureCard(feature = feature)
                    }
                }

                Spacer(modifier = Modifier.height(120.dp))

            }

        }
    }
}

@Composable
private fun EnhancedHeaderSection(
    onProfileClick: () -> Unit,
    onUpgradeClick: () -> Unit
) {
    val context = LocalContext.current
    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }
    val authState by userViewModel.authState.collectAsState()


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
            .statusBarsPadding(),
    ){
        Row(
           modifier = Modifier.padding(10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Good Morning! 👋",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "AI Features",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }

            if(authState?.user?.isPremium == true) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onUpgradeClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .shadow(8.dp, RoundedCornerShape(24.dp)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Upgrade",
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pro",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search AI features, tools, guides...",
                color = Color(0xFF718096),
                fontSize = 16.sp
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF718096),
                modifier = Modifier.size(22.dp)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color(0xFF718096),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(8.dp, RoundedCornerShape(30.dp)),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color(0xFF2D3748),
            unfocusedTextColor = Color(0xFF2D3748)
        ),
        singleLine = true
    )
}

@Composable
private fun EnhancedMainFeatureCard(
    feature: FeatureCard,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_scale"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 12.dp
    ) {
        Card(
            modifier = modifier
                .height(180.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .clickable {
                    isPressed = true
                    feature.onClick()
                },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(feature.gradient)
                    )
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            feature.icon,
                            contentDescription = feature.title,
                            modifier = Modifier.size(28.dp),
                            tint = Color.White
                        )
                    }

                    Column {
                        Text(
                            text = feature.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = feature.subtitle,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium
                        )
                        if (feature.description.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = feature.description,
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                lineHeight = 16.sp
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
private fun EnhancedSecondaryFeatureCard(
    feature: FeatureCard,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "secondary_card_scale"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp
    ) {
        Card(
            modifier = modifier
                .height(120.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .clickable {
                    isPressed = true
                    feature.onClick()
                },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(feature.gradient)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        feature.icon,
                        contentDescription = feature.title,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )

                    Column {
                        Text(
                            text = feature.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = feature.subtitle,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
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
private fun EnhancedSmallFeatureCard(
    feature: SmallFeature
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "small_card_scale"
    )

    Surface(
        shape = CircleShape,
        shadowElevation = 0.dp,
        modifier = Modifier.background(Color.Transparent)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .clickable {
                    isPressed = true
                    feature.onClick()
                }
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    feature.icon,
                    contentDescription = feature.title,
                    modifier = Modifier.size(32.dp),
                    tint = feature.iconColor
                )
            }

            Text(
                text = feature.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748),
                textAlign = TextAlign.Center
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}


