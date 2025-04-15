package com.example.hodos_final_android.component


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ImageCarousel(
    lstImgUrl: List<String>,
    modifier: Modifier = Modifier,
    autoScrollDuration: Long = 3000,
    enableAutoScroll: Boolean = true,
    showIndicator: Boolean = true,
    activeIndicatorColor: Color = Color(0xFF2196F3),
    inactiveIndicatorColor: Color = Color.LightGray,
    contentDescription: String? = null,
    rounded: Int? = 20
) {
    if (lstImgUrl.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { lstImgUrl.size })
    val coroutineScope = rememberCoroutineScope()

    // Auto scroll logic
    LaunchedEffect(pagerState, enableAutoScroll) {
        if (enableAutoScroll && lstImgUrl.size > 1) {
            while (true) {
                delay(autoScrollDuration)
                val nextPage = (pagerState.currentPage + 1) % lstImgUrl.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Image Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            if (rounded != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(rounded.dp)
                ) {
                    GlideImage(
                        model = lstImgUrl[page],
                        contentDescription = contentDescription ?: "Carousel image $page",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Indicators
        if (showIndicator && lstImgUrl.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(lstImgUrl.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    val size by animateDpAsState(
                        targetValue = if (isSelected) 10.dp else 8.dp,
                        label = "indicator size"
                    )

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(size)
                            .clip(CircleShape)
                            .background(if (isSelected) activeIndicatorColor else inactiveIndicatorColor)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

// Extension function to scroll to a specific page
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.scrollToPage(page: Int) {
    if (pageCount > 0) {
        val targetPage = page.coerceIn(0, pageCount - 1)
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            animateScrollToPage(targetPage)
        }
    }
}
@Composable
fun CarouselExample(
    height: Int? = 220,
    rounded: Int? = 20,
    banners: List<String>? = null
) {
    val imageUrls = banners ?: emptyList()

    if (height != null) {
        ImageCarousel(
            rounded = rounded ?: 0,
            lstImgUrl = imageUrls,
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp),
            autoScrollDuration = 5000,
            enableAutoScroll = true,
            activeIndicatorColor = MaterialTheme.colorScheme.primary,
            inactiveIndicatorColor = MaterialTheme.colorScheme.secondary
        )
    }
}
