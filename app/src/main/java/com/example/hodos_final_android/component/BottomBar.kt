package com.example.hodos_final_android.component


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.hodos_final_android.R
import com.example.hodos_final_android.screen.BottomBar
import com.example.hodos_final_android.theme.HodosTheme
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun BottomBarComponent(navController: NavController) {


    NavComponent(
        nav = navController,

    )
}


@Composable
fun NavComponent(
    nav: NavController,

) {
    CustomBottomNavigation(nav)

}


@Composable
fun CustomBottomNavigation(nav: NavController) {
    val currentRoute = nav.currentBackStackEntryAsState().value?.destination?.route

    HodosTheme {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                val icons = listOf(
                    painterResource(R.drawable.home_alt_svgrepo_com) to BottomBar.Home.route,
                    painterResource(R.drawable.search_alt_svgrepo_com) to BottomBar.Search.route,
                    painterResource(R.drawable.fellow_activity_21_svgrepo_com) to BottomBar.Activity.route,
                    painterResource(R.drawable.profile_svgrepo_com) to BottomBar.Profile.route
                )

                icons.forEach { (icon, route) ->
                    val isActive = currentRoute == route

                    IconButton(onClick = {
                        nav.navigate(route)
                    }) {
                        Icon(
                            painter = icon,
                            contentDescription = null,
                            tint = if (isActive) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

            }
        }
    }
}



@Composable
fun AnimatedFab(
    modifier: Modifier,
    icon: ImageVector? = null,
    opacity: Float = 1f,
    onClick: () -> Unit = {}
) {
   HodosTheme {
       FloatingActionButton(
           onClick = onClick,
           elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
           modifier = modifier.scale(1.25f),
           shape = RoundedCornerShape(100), // Custom corner radius
           containerColor = MaterialTheme.colorScheme.secondary, // Custom background color
       ) {
           icon?.let {
               Icon(
                   imageVector = it,
                   contentDescription = null,
                   tint = MaterialTheme.colorScheme.primary
               )
           }
       }
   }
}
@Composable
fun FloatingActionGroup() {
    val isMenuExtended = remember { mutableStateOf(false) }

    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearEasing
        )
    )

    val animatedBlur by animateDpAsState(
        targetValue = if (isMenuExtended.value) 10.dp else 0.dp, // Mờ nền khi mở menu
        animationSpec = tween(durationMillis = 200)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(2f),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Chỉ hiện nền mờ khi menu mở
        if (isMenuExtended.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)) // Nền tối khi mở menu
                    .blur(animatedBlur) // Áp dụng hiệu ứng mờ
                    .clickable { isMenuExtended.value = false } // Đóng menu khi bấm ra ngoài
            )
        }

        Box(
            modifier = Modifier.padding(20.dp)
        ) {
            FabGroup(
                animationProgress = fabAnimationProgress,
                toggleAnimation = { isMenuExtended.value = !isMenuExtended.value }
            )
        }
    }
}


@Composable
fun FabGroup(
    animationProgress: Float = 0f,
    toggleAnimation: () -> Unit = { }
) {
    val fabCount = 3 // Số lượng FAB phụ
    val maxRadius = 100.dp // Bán kính tối đa khi mở hết

    for (i in 0 until fabCount) {
        val angle = (i * 45) // Các góc: 0°, 45°, 90°
        val radian = Math.toRadians(angle.toDouble())

        val radius = (animationProgress * maxRadius.value).dp // FAB bắt đầu từ 0 và mở dần ra ngoài
        val offsetX = (cos(radian) * radius.value).dp
        val offsetY = (sin(radian) * radius.value).dp

        AnimatedFab(
            icon = when (i) {
                0 -> Icons.Default.PhotoCamera
                1 -> Icons.Default.Settings
                else -> Icons.Default.ShoppingCart
            },
            modifier = Modifier
                .offset(x = -offsetX, y = -offsetY)
                .alpha(animationProgress), // FAB sẽ mờ dần khi thu gọn
            onClick = {
                when (i) {
                    0 -> {
                        // Hành động khi bấm vào Camera
                        println("PhotoCamera clicked")
                    }
                    1 -> {
                        // Hành động khi bấm vào Settings
                        println("Settings clicked")
                    }
                    2 -> {
                        // Hành động khi bấm vào ShoppingCart
                        println("ShoppingCart clicked")
                    }
                }
            }
        )

    }

    // FAB chính (nút mở menu)
    AnimatedFab(
        icon = Icons.Default.Add,
        modifier = Modifier.rotate(225 * animationProgress),
        onClick = toggleAnimation,
    )
}







