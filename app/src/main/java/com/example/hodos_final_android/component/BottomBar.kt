package com.example.hodos_final_android.component


import android.view.animation.OvershootInterpolator
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.component.BottomBar.BellColorButton
import com.example.hodos_final_android.component.BottomBar.ButtonBackground
import com.example.hodos_final_android.component.BottomBar.ColorButtonAnimation
import com.example.hodos_final_android.di.AppStateViewEntryPoint
import com.example.hodos_final_android.screen.BottomBarRoute
import com.example.hodos_final_android.theme.HodosTheme
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import dagger.hilt.android.EntryPointAccessors
import kotlin.math.cos
import kotlin.math.sin


const val Duration = 500
const val DoubleDuration = 1000

@Stable
data class Item(
    @DrawableRes val icon: Int,
    var isSelected: Boolean,
    @StringRes val description: Int,
    val animationType: ColorButtonAnimation = BellColorButton(
        tween(500),
        background = ButtonBackground(R.drawable.plus)
    ),
    val route: String
)

val dropletButtons = listOf(
    Item(
        icon = R.drawable.home,
        isSelected = false,
        description = R.string.Home,
        route = BottomBarRoute.Home.route
    ),
    Item(
        icon = R.drawable.trip,
        isSelected = false,
        description = R.string.Bell,
        route = BottomBarRoute.Trip.route

    ),
    Item(
        icon = R.drawable.activity,
        isSelected = false,
        description = R.string.Message,
        route = BottomBarRoute.Activity.route

    ),
    Item(
        icon = R.drawable.event,
        isSelected = false,
        description = R.string.Heart,
        route = BottomBarRoute.Event.route

    ),
    Item(
        icon = R.drawable.person,
        isSelected = false,
        description = R.string.Person,
        route = BottomBarRoute.Profile.route

    ),
)

@Composable
fun BottomBarComponent(navController: NavController) {
    val context = LocalContext.current
    val appStateViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, AppStateViewEntryPoint::class.java)
            .appStateViewModel()
    }

    val appState by appStateViewModel.appState.collectAsState()

    AnimatedNavigationBar(
        modifier = Modifier
            .padding(horizontal = 0.dp, vertical = 0.dp)
            .height(85.dp),
        selectedIndex = appState.selectTabIndex,
        ballColor = Color.White,
        cornerRadius = shapeCornerRadius(25.dp),
        ballAnimation = Parabolic(tween(Duration, easing = LinearOutSlowInEasing)),
        indentAnimation = Height(
            indentWidth = 56.dp,
            indentHeight = 15.dp,
            animationSpec = tween(
                DoubleDuration,
                easing = { OvershootInterpolator().getInterpolation(it) })
        )
    ) {
            dropletButtons.forEachIndexed { index, it ->
                DropletButton(
                    modifier = Modifier.fillMaxSize(),
                    isSelected = appState.selectTabIndex == index,
                    onClick = {
                        appStateViewModel.onSelectTab(index)
                        navController.navigate(it.route)
                    },
                    icon = it.icon,
                    dropletColor = MaterialTheme.colorScheme.primary,
                    animationSpec = tween(durationMillis = Duration, easing = LinearEasing),
                )
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







