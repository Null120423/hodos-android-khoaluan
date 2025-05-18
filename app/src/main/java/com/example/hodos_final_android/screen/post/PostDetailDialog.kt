
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.HighlightedContent
import com.example.hodos_final_android.component.ProfileAvatar
import com.example.hodos_final_android.component.RowStart
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.helper.getTimeAgo
import com.example.hodos_final_android.model.Post
import com.example.hodos_final_android.navigateWithAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostDetailDialog(
    post: Post,
    onDismiss: () -> Unit
) {
    val navController = LocalNavController.current
    fun handleClickLabel(label: String) {
        val labelNotTag = label.substring(1)
        val locationByTag = post.locations.find { it -> it.label == labelNotTag }

        if(locationByTag != null ){
            navController.navigateWithAnimation(Screen.LocationDetailScreen.createRoute(locationByTag.id))
        }

    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .clickable(onClick = onDismiss)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.BottomCenter)
                    .clickable(enabled = false) { }
                    .background(
                        color = MaterialTheme.colorScheme.background,
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    // Image carousel
                    val pagerState = rememberPagerState(pageCount = { post.imgs.size })

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(post.imgs[page])
                                    .crossfade(true)
                                    .build()
                            ),
                            contentDescription = "Post image ${page + 1}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Top navigation bar with back button and bookmark
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Back button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        // Bookmark button
                        IconButton(
                            onClick = { /* Bookmark action */ },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = Color.White
                            )
                        }
                    }

                    // Page indicator at bottom
                    if (post.imgs.size > 1) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(post.imgs.size) { index ->
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .size(width = if (pagerState.currentPage == index) 24.dp else 8.dp, height = 8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            if (pagerState.currentPage == index)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.secondary
                                        )
                                )
                            }
                        }
                    }
                }

                // Post title and content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    RowStart {
                        ProfileAvatar(
                            letter = post?.username?.firstOrNull()?.uppercase() ?: "?",
                            backgroundColor = Color(0xFF4CAF50),
                            modifier = Modifier.size(32.dp)
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {
                            post?.username?.let {
                                Txt(
                                    value = it.capitalize(),
                                    fontWeight = FontWeight.Bold,
                                    size = 16
                                )
                            }
                            Txt(
                                value = post.createdAt.getTimeAgo(),
                            )
                        }
                    }
                    Seprate(height = 10)

                    // Caption
                    post?.content?.let {
                        HighlightedContent(text = it) { tag ->
                            // Xử lý khi bấm vào hashtag
                            println("Clicked tag: $tag")
                            handleClickLabel(tag)
                        }
                    }

                    Seprate(height = 10)

                    // Action buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        var isLiked by remember { mutableStateOf(false) }
                        val scope = rememberCoroutineScope()
                        val scale by animateFloatAsState(
                            targetValue = if (isLiked) 1.2f else 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "Like Animation"
                        )

                        // Like button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Favorite,
                                contentDescription = "Like",
                                tint = if (isLiked) Color.Red else Color.Gray,
                                modifier = Modifier
                                    .size(28.dp)
                                    .scale(scale)
                                    .clickable {
                                        isLiked = !isLiked
                                        if (isLiked) {
                                            scope.launch {
                                                delay(300)
                                            }
                                        }
                                    }
                            )
                        }

                        // Comment button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = "Comment",
                                tint = Color.Gray,
                                modifier = Modifier.size(28.dp)
                                    .clickable {
                                        isLiked = !isLiked
                                        if (isLiked) {
                                            scope.launch {
                                                delay(300)
                                            }
                                        }
                                    }
                            )
                        }

                        // Share button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Send,
                                contentDescription = "Share",
                                tint = Color.Gray,
                                modifier = Modifier.size(28.dp)
                                    .clickable {
                                        isLiked = !isLiked
                                        if (isLiked) {
                                            scope.launch {
                                                delay(300)
                                            }
                                        }
                                    }
                            )
                        }
                    }

                    Divider(color = Color.LightGray.copy(alpha = 0.5f))

                }
            }
        }
    }
}
