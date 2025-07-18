package com.example.hodos_final_android.screen.profile

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.ColumnCenter
import com.example.hodos_final_android.component.EmailVerificationCard
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.ProfileAvatar
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.di.UserViewModelEntryPoint
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.model.GetUserInfoModel
import com.example.hodos_final_android.model.RegisterModel
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.screen.RequireLoginScreen
import com.example.hodos_final_android.view_model.AuthViewModel
import dagger.hilt.android.EntryPointAccessors

@Composable
fun ProfileScreen(
    isLoggedIn: Boolean = true,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val navController = LocalNavController.current
    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }
    val authState by userViewModel.authState.collectAsState()
    val isConfirmLogout = remember { mutableStateOf(false) }
    val planData = userViewModel.getSuggestPricingPlan()


    fun handleResetLogin() {
        val accessToken = authState?.accessToken
        val refreshToken = authState?.refreshToken


        // If either token is missing, cannot proceed
        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) return

        val getUserInfoModel = GetUserInfoModel(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
        authViewModel.userInfo(getUserInfoModel)
    }


    LaunchedEffect(Unit) {
        handleResetLogin()
    }

    if (userViewModel.getAccessToken() == null) {
        RequireLoginScreen()
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(0.7f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.statusBarsPadding()
                ){
                    Seprate(height = 5)
                }

                // Header Section with Profile
                authState?.user?.username?.let {
                    ModernProfileHeader(
                        username = it,
                        avatar = authState?.user?.avatar,
                        isPremium = authState?.user?.isPremium == true,
                        onEditClick = {
                            navController.navigateWithAnimation(Screen.UpdateProfileScreen.route)
                        }
                    )
                }
                // Stats Card
                ModernStatsCard(isLoggedIn = isLoggedIn)

                Spacer(modifier = Modifier.height(20.dp))

                // Content Cards
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Email Verification Card
                    val user = authState?.user
                    if (user != null) {
                        EmailVerificationCard(
                            isVisible = isLoggedIn && user.isNeedVerify,
                            userEmail = user.email,
                            onVerifyClick = {
                                val registerModel = RegisterModel(
                                    username = user.username,
                                    email = user.email,
                                    password = "",
                                    confirmPassword = ""
                                )
                                navController.navigateWithAnimation(Screen.EmailVerification.createRoute(registerModel))
                            },
                            onDismiss = {},
                        )
                    }

                    // Main Menu Card
                    ModernMenuCard()

                    // Settings Menu Card
                    ModernSecondaryMenuCard()

                    // Premium/Subscription Card
                    if (isLoggedIn) {
                        if (authState?.user != null && authState?.user!!.isPremium == true) {
                            val pricingPlan = authState?.user?.userSubscription?.pricingPlan
                            if (pricingPlan != null) {
                                SubscriptionManagementCard(currentUser = authState?.user!!, planData = pricingPlan)
                            }
                        } else {
                            if (planData != null) {
                                PremiumUpgradeCard(planData = planData)
                            }
                        }
                    } else {
                        ModernReferralCard()
                    }

                    // Logout Button
                    if (userViewModel.getAccessToken() != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.9f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            BtnPrimary(
                                backgroundColor = Color(0xFFFF5252),
                                title = "Sign Out",
                                onClick = {
                                    isConfirmLogout.value = true
                                },
                                minWidth = getScreenWidth() - 64,
                                size = 16,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        if (isConfirmLogout.value) {
            ConfirmLogoutDialog(
                onDismiss = {
                    isConfirmLogout.value = false
                },
                onConfirm = {
                    isConfirmLogout.value = false
                    userViewModel.logout()
                }
            )
        }
    }
}

@Composable
fun ModernProfileHeader(
    username: String,
    avatar: String? = null,
    isPremium: Boolean = false,
    onEditClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                ProfileAvatar(
                    letter = username.substring(0, 1).uppercase(),
                    backgroundColor = Color(0xFF667eea),
                    size = 100,
                    url = avatar
                )

                if (isPremium) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFFFFD700), Color(0xFFFFA000))
                                ),
                                shape = CircleShape
                            )
                            .padding(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Premium",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = username.replaceFirstChar { it.uppercase() },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFF667eea).copy(alpha = 0.1f),
                            CircleShape
                        )
                        .clickable { onEditClick() }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color(0xFF667eea),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (isPremium) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Premium Member",
                    fontSize = 14.sp,
                    color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ModernStatsCard(isLoggedIn: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ModernStatItem(
                icon = Icons.Outlined.TravelExplore,
                value = "12",
                label = "Trips",
                color = Color(0xFF4CAF50)
            )

            Divider(
                modifier = Modifier
                    .height(60.dp)
                    .width(1.dp),
                color = Color(0xFFE0E0E0)
            )

            ModernStatItem(
                icon = Icons.Outlined.Wallet,
                value = if (isLoggedIn) "12" else "-",
                label = "Hodos Xu",
                color = Color(0xFF2196F3)
            )

            Divider(
                modifier = Modifier
                    .height(60.dp)
                    .width(1.dp),
                color = Color(0xFFE0E0E0)
            )

            ModernStatItem(
                icon = Icons.Outlined.CardGiftcard,
                value = if (isLoggedIn) "120" else "-",
                label = "Gift Cards",
                color = Color(0xFFFF9800)
            )
        }
    }
}

@Composable
fun ModernStatItem(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color.copy(alpha = 0.1f),
                    CircleShape
                )
                .padding(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3748)
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF718096)
        )
    }
}

@Composable
fun ModernMenuCard() {
    val navController = LocalNavController.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "My Account",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ModernMenuItem(
                icon = Icons.Default.Edit,
                title = "Update Profile",
                subtitle = "Edit your personal information",
                color = Color(0xFF667eea),
                onClick = {
                    navController.navigateWithAnimation(Screen.UpdateProfileScreen.route)
                }
            )

            ModernMenuItem(
                icon = Icons.Default.AirplanemodeActive,
                title = "My Trips",
                subtitle = "View your travel history",
                color = Color(0xFF4CAF50)
            )

            ModernMenuItem(
                icon = Icons.Default.Newspaper,
                title = "My Posts",
                subtitle = "Manage your shared content",
                color = Color(0xFFFF9800)
            )
        }
    }
}

@Composable
fun ModernSecondaryMenuCard() {
    val navController = LocalNavController.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Support & Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            ModernMenuItem(
                icon = Icons.Default.PrivacyTip,
                title = "Terms & Security",
                subtitle = "Get help and contact us",
                color = MaterialTheme.colorScheme.primary,
                onClick = {
                    navController.navigateWithAnimation(Screen.TermAndSecurity.route)
                }
            )

            ModernMenuItem(
                icon = Icons.Default.Help,
                title = "Help & Support",
                subtitle = "Get help and contact us",
                color = Color(0xFF9C27B0)
            )

            ModernMenuItem(
                icon = Icons.Default.Settings,
                title = "Settings",
                subtitle = "App preferences and privacy",
                color = Color(0xFF607D8B)
            )
        }
    }
}

@Composable
fun ModernMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit = {}
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(),
        label = "MenuItem Scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color.copy(alpha = 0.1f),
                        CircleShape
                    )
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = color
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3748)
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color(0xFF718096)
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF718096),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
fun ModernReferralCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Invite Friends & Earn Rewards",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Share Hodos with friends and get exclusive benefits",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                BtnPrimary(
                    backgroundColor = Color.White,
                    textColor = Color(0xFF667eea),
                    title = "Start Inviting",
                    minWidth = getScreenWidth() - 80,
                    size = 16
                )
            }
        }
    }
}
