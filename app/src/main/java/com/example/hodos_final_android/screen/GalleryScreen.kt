
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.component.Header
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.helper.getScreenHeight
import com.example.hodos_final_android.helper.getScreenWidth
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GalleryFullScreen() {
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val images = navController.previousBackStackEntry?.savedStateHandle?.get<List<String>>("images") ?: emptyList()
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Box {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            itemsIndexed(images) { index, imageUrl ->
                ImgWithUrl(
                    url = imageUrl,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(0.dp))
                        .clickable { selectedIndex = index },
                    rounded = 0
                )
            }
        }

            Header()

        selectedIndex?.let { startIndex ->
            val pagerState = rememberPagerState(initialPage = startIndex)

            Dialog(onDismissRequest = { selectedIndex = null },
                properties = androidx.compose.ui.window.DialogProperties(
                    usePlatformDefaultWidth = false // <- mở rộng hết màn hình
                ),
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .width(getScreenWidth().dp)
                            .height(getScreenHeight().dp)
                    ) {
                        HorizontalPager(
                            count = images.size,
                            state = pagerState
                        ) { page ->
                            ZoomableImage(
                                url = images[page], coroutineScope = coroutineScope,
                                pagerState = pagerState,
                                pageIndex = currentPage
                            )
                        }

                        // Arrow Buttons
                        if (pagerState.currentPage > 0) {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 8.dp)
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Previous", tint = MaterialTheme.colorScheme.background)
                            }
                        }

                        if (pagerState.currentPage < images.lastIndex) {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 8.dp)
                            ) {
                                Icon(Icons.Default.ArrowForward, contentDescription = "Next", tint = MaterialTheme.colorScheme.background)
                            }
                        }
                    }
                }
                )
        }
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ZoomableImage(
    url: String,
    pagerState: PagerState,
    pageIndex: Int,
    coroutineScope: CoroutineScope
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val screenWidth = getScreenWidth().toFloat()

    ImgWithUrl(
        url = url,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(1f, 5f)
                    offsetX += pan.x
                    offsetY += pan.y

                    // Khi scale = 1 (đang không zoom) mới xử lý swipe
                    if (scale == 1f) {
                        val threshold = screenWidth * 0.1f // 10% chiều rộng

                        if (offsetX > threshold && pageIndex > 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pageIndex - 1)
                            }
                            offsetX = 0f
                        } else if (offsetX < -threshold && pageIndex < pagerState.pageCount - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pageIndex + 1)
                            }
                            offsetX = 0f
                        }
                    }
                }
            }
    )
}

