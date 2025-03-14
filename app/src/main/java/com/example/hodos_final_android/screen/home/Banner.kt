package com.example.hodos_final_android.screen.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.hodos_final_android.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue

@Preview()
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top= 20.dp)
    ) {
        // Add other UI components here as needed

        ImageSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        )
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(modifier: Modifier = Modifier) {
    val imageList = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner4,
        R.drawable.banner_3,
        R.drawable.banner5
    )


    val pagerState = rememberPagerState(initialPage = 1, pageCount = { imageList.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 48.dp), // Để lộ hai item hai bên
        pageSpacing = -10.dp, // Khoảng cách giữa các item
    ) { page ->
        val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
        val scale = 0.85f + (1f - minOf(1f, abs(pageOffset))) * 0.15f // Scale giảm dần
        val alpha = 0.5f + (1f - minOf(1f, abs(pageOffset))) * 0.5f  // Alpha giảm dần

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9) // Tỉ lệ khung hình
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .clip(RoundedCornerShape(16.dp)) // Bo góc item
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                .background(Color.Gray)
        ) {
            Image(
                painter = painterResource(id = imageList[page]),
                contentDescription = "Banner image ${page + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().height(500.dp)
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun calculateCurrentPageScale(pagerState: PagerState, page: Int): Float {
    val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

    // Calculate scale similar to the original ViewPager2 transformer
    val scale by animateFloatAsState(
        targetValue = 0.85f + (1f - pageOffset.coerceIn(0f, 1f)) * 0.14f,
        label = "page_scale"
    )

    return scale
}