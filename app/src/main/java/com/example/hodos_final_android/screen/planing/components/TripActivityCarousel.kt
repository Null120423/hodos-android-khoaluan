package com.example.hodos_final_android.screen.planing.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.model.TripActivity
import kotlin.math.absoluteValue

@Composable
fun TripActivityCarousel(
    modifier: Modifier = Modifier,
    tripActivities: List<TripActivity>,
    backgroundColor: Color = Color(0xFF6C63FF) // Purple background color like in the image
) {
    val pagerState = rememberPagerState { tripActivities.size }
    val configuration = LocalConfiguration.current

    // Calculate card width to ensure proper side item visibility
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = screenWidth * 0.75f // Center card takes 75% of screen width
    val sideItemVisibleWidth = (screenWidth - cardWidth) / 2 // Each side item shows equally

    Box(
        modifier = modifier
            .background(backgroundColor)
            .defaultMinSize(minHeight = 300.dp)
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 0.dp, // No spacing between pages for a cleaner look
            contentPadding = PaddingValues(horizontal = sideItemVisibleWidth) // Dynamic padding based on screen size
        ) { page ->
            val activity = tripActivities[page]

            // Calculate current offset
            val pageOffset = (
                    (pagerState.currentPage - page) + pagerState
                        .currentPageOffsetFraction
                    ).absoluteValue

            // Animate the transformation values
            val animatedScale by animateFloatAsState(
                targetValue = lerp(0.85f, 1f, 1f - pageOffset.coerceIn(0f, 1f)),
                label = "ScaleAnim"
            )

            val animatedAlpha by animateFloatAsState(
                targetValue = lerp(0.7f, 1f, 1f - pageOffset.coerceIn(0f, 1f)),
                label = "AlphaAnim"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .width(cardWidth) // Fixed width based on screen size
                    .clip(RoundedCornerShape(24.dp)) // Rounded corners like in the image
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                        alpha = animatedAlpha
                    }
            ) {
                // Use the existing TripActivityCardMap but with a white background
                TripActivityCardMap(
                    activity = activity,
                    dayNumber = 1,
                    onDirectionsClick = {},
                    modifier = Modifier
                        .background(Color.White) // White background like in the image
                        .fillMaxWidth()
                )
            }
        }
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + (stop - start) * fraction
}
