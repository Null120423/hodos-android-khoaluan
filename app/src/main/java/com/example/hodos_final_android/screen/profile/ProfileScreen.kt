package com.example.hodos_final_android.screen.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.TextBtn
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.navigateWithAnimation

@Composable
fun ProfileScreen(
    isLoggedIn: Boolean = true
) {
    val scrollState = rememberScrollState()
    MainLayout(
        header = false,
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
        content = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!isLoggedIn) {
                    LoggedInHeader()
                } else {
                    LoggedOutHeader()
                }
                // Stats card
                StatsCard(isLoggedIn)

                // Menu items
                MenuCard()

                // Secondary menu items
                SecondaryMenuCard()

                // Promo banner
                if (isLoggedIn) {
                    LocationPromoBanner()
                } else {
                    ReferralBanner()
                }

                Seprate(height = 100)

            }
        }
    )
}

@Composable
fun LoggedOutHeader() {
    val navController = LocalNavController.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Profile image
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                TextBtn(
                    title = "Sign In / Sign Up",
                    size = 20,
                    onClick = {
                        navController.navigateWithAnimation(Screen.Login.route)
                    }
                )
            }

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun LoggedInHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile image
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Txt(
                        value = "Huu Tai",
                        size = 18,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 76.dp)
                ) {
                    Txt(
                        value = "Update profile",
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
@Composable
fun StatsCard(isLoggedIn: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                value = "12",
                label = "Trips"
            )

            Divider(
                modifier = Modifier
                    .height(60.dp)
                    .width(1.dp)
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.tertiary
            )

            StatItem(
                value = if (isLoggedIn) "12" else "-",
                label = "Hodos Xu"
            )

            Divider(
                modifier = Modifier
                    .height(60.dp)
                    .width(1.dp)
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.tertiary
            )

            StatItem(
                value = if (isLoggedIn) "120" else "-",
                label = "Gift card"
            )
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun MenuCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MenuItem(
                icon = Icons.Default.Receipt,
                title = "Trips"
            )

            MenuItem(
                icon = Icons.Default.Person,
                title = "Profile",
            )

            MenuItem(
                icon = Icons.Default.Star,
                title = "Rating"
            )
        }
    }
}

@Composable
fun SecondaryMenuCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MenuItem(
                icon = Icons.Default.Help,
                title = "Support"
            )

            MenuItem(
                icon = Icons.Default.Settings,
                title = "Settings"
            )
        }
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Txt(
                        value = title,
                        size = 16
                    )

                    if (subtitle != null) {
                        Spacer(modifier = Modifier.height(2.dp))

                        Txt(
                            value = subtitle,
                            size = 12,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }


    }
}

@Composable
fun ReferralBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Txt(
                value = "Invite your friends",
                size = 18,
            )
            Spacer(modifier = Modifier.height(12.dp))

            BtnPrimary(
                minWidth = getScreenWidth() - 20,
                title = "Watch detail"
            )
        }
    }
}

@Composable
fun LocationPromoBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Txt(
                    value = "Get local tips",
                    size = 18,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background

                )

                Spacer(modifier = Modifier.height(4.dp))

                Txt(
                    value = "Add your country/region to get better recommendations",
                    size = 14,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // World map icon with location pins
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.scrim
                )

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .offset(x = 20.dp, y = (-10).dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}