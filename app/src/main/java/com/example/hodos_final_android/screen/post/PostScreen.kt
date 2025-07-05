package com.example.hodos_final_android.screen.post

import PostDetailDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.EmptyView
import com.example.hodos_final_android.component.HighlightedContent
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.component.ProfileAvatar
import com.example.hodos_final_android.di.PostViewModelEntryPoint
import com.example.hodos_final_android.helper.getTimeAgo
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.Post
import com.example.hodos_final_android.navigateWithAnimation
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class StoryItem(
    val id: String,
    val username: String,
    val profileImageUrl: String,
    val hasUnseenStory: Boolean = false
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PostScreen() {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val postViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, PostViewModelEntryPoint::class.java)
            .postViewModel()
    }

    val paginationPostState by postViewModel.paginationState.collectAsState()

    var isLoadingMore by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        postViewModel.pagination(
            Pagination(
                skip = 0,
                take = 20,
                where = {}
            )
        )
        delay(500)
        refreshing = false
    }

    val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)
    var selectedPost by remember { mutableStateOf<Post?>(null) }

    // Enhanced sample data with more realistic profiles
    val storyItems = remember {
        listOf(
            StoryItem("add", "Your Story", "https://cdn.dribbble.com/users/1565678/avatars/normal/f7141e584986ea624b56d2eaada3e330.jpg?1643382230", false),
            StoryItem("1", "Kate Mary", "https://cdn.dribbble.com/users/4137552/avatars/normal/d151a2d9509323de835e33009de57589.png?1684156519", true),
            StoryItem("2", "Jacki Hall", "https://cdn.dribbble.com/users/2616092/avatars/normal/bafec5669a661504f172a0813d464139.jpg?1721321274", true),
            StoryItem("3", "Amy Adam", "https://cdn.dribbble.com/users/3112201/avatars/normal/bd7fe692d89ca29944a4549bab12e17a.jpg?1628089893", false),
            StoryItem("4", "James Love", "https://cdn.dribbble.com/users/21506638/avatars/normal/dbb5eb345db100e6fa63dfd173d3c31f.png?1728071059", true),
            StoryItem("5", "Sarah Kim", "https://cdn.dribbble.com/users/1565678/avatars/normal/f7141e584986ea624b56d2eaada3e330.jpg?1643382230", false)
        )
    }

    LaunchedEffect(Unit) {
        if(paginationPostState.data == null ) {
            postViewModel.pagination(
                Pagination(
                    skip = 0,
                    take = 20,
                    where = {}
                )
            )
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .pullRefresh(pullRefreshState)
    ) {
        val posts = paginationPostState.data?.data ?: emptyList()

        if (posts.isEmpty() && !paginationPostState.isLoading) {
            EmptyView(title = "No posts yet! Start sharing your adventures 🌟")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                ,
                contentPadding = PaddingValues(bottom = 16.dp)

            ) {
                stickyHeader {
                    // Modern App Header
                    ModernAppHeader()
                }

//                item {
//                    // Enhanced Stories Section
//                    EnhancedStoriesSection(storyItems)
//                }

                itemsIndexed(posts) { index, post ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(300, delayMillis = index * 50)
                        ) + fadeIn(animationSpec = tween(300))
                    ) {
                        ModernPostCard(
                            post = post,
                            onPostClick = { selectedPost = post }
                        )
                    }
                }

                if (paginationPostState.data?.hasNext == true) {
                    item {
                        ModernLoadMoreIndicator {
                            if (!isLoadingMore) {
                                isLoadingMore = true
                                postViewModel.pagination(
                                    Pagination(
                                        skip = paginationPostState.data?.nextSkip ?: 0,
                                        take = paginationPostState.data?.take ?: 20,
                                        where = {}
                                    )
                                )
                                isLoadingMore = false
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Modern Floating Action Button
        ModernFAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 16.dp)
        ) {
            navController.navigateWithAnimation(Screen.CreatePostScreen.route)
        }

        // Enhanced Pull Refresh Indicator
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.surface
        )

        if (paginationPostState.isLoading && paginationPostState.data == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Loading(title = "Loading amazing posts...")
            }
        }

        if(paginationPostState.error != null ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "😔",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Oops! Something went wrong",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = paginationPostState.error!!.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }

    // Post Detail Dialog
    if (selectedPost != null) {
        PostDetailDialog(
            post = selectedPost!!,
            onDismiss = { selectedPost = null }
        )
    }
}

@Composable
fun ModernAppHeader() {
    Row(
        modifier = Modifier
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
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ){
            Text(
                text = "Hodos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
fun EnhancedStoriesSection(stories: List<StoryItem>) {
    Column {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(stories) { story ->
                EnhancedStoryItem(story)
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            thickness = 0.5.dp
        )
    }
}

@Composable
fun EnhancedStoryItem(story: StoryItem) {
    val context = LocalContext.current
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .scale(scale)
            .clickable {
                isPressed = !isPressed
                // TODO: Open story
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            // Gradient ring for unseen stories
            if (story.hasUnseenStory) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE91E63),
                                    Color(0xFFFF9800),
                                    Color(0xFF9C27B0)
                                )
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                )
            }

            // Profile image
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(story.profileImageUrl)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = "Profile of ${story.username}",
                modifier = Modifier
                    .size(if (story.hasUnseenStory) 60.dp else 64.dp)
                    .clip(CircleShape)
                    .border(
                        width = if (story.hasUnseenStory) 0.dp else 2.dp,
                        color = if (story.hasUnseenStory) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )

            // Add icon for "Your Story"
            if (story.id == "add") {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add story",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        Text(
            text = story.username,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ModernPostCard(post: Post, onPostClick: () -> Unit) {
    val navController = LocalNavController.current
    var isLiked by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }

    fun handleClickLabel(label: String) {
        val labelNotTag = label.substring(1)
        val locationByTag = post.locations.find { it.label == labelNotTag }

        if(locationByTag != null ){
            navController.navigateWithAnimation(Screen.LocationDetailScreen.createRoute(locationByTag.id))
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onPostClick,
        shape = RectangleShape
    ) {
        Column {
            // Modern Post Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileAvatar(
                    letter = post.username?.firstOrNull()?.uppercase() ?: "?",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = post.username?.capitalize() ?: "Unknown User",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = post.createdAt.getTimeAgo(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { /* TODO: Show options menu */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Enhanced Caption
            if (post.content?.isNotEmpty() == true) {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    HighlightedContent(text = post.content) { tag ->
                        handleClickLabel(tag)
                    }
                }
            }

            // Enhanced Image Layout
            EnhancedImageLayout(post.imgs)

            // Modern Action Bar
            ModernActionBar(
                isLiked = isLiked,
                isSaved = isSaved,
                likeCount = post.timePosted,
                commentCount = post.commentCount,
                onLikeClick = { isLiked = !isLiked },
                onCommentClick = { /* TODO: Open comments */ },
                onShareClick = { /* TODO: Share post */ },
                onSaveClick = { isSaved = !isSaved }
            )
        }
    }
}

@Composable
fun EnhancedImageLayout(images: List<String>) {
    when (images.size) {
        0 -> return
        1 -> {
            ImgWithUrl(
                url = images.first(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentScale = ContentScale.Crop
            )
        }
        2 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                images.take(2).forEach { url ->
                    ImgWithUrl(
                        url = url,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(end = if (url == images.first()) 1.dp else 0.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        3 -> {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    images.take(2).forEach { url ->
                        ImgWithUrl(
                            url = url,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(end = if (url == images.first()) 1.dp else 0.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
                ImgWithUrl(
                    url = images[2],
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        else -> {
            val gridImages = images.take(4)
            Column {
                for (row in gridImages.chunked(2)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        for ((index, url) in row.withIndex()) {
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
                                ImgWithUrl(
                                    url = url,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(
                                            end = if (index == 0) 1.dp else 0.dp,
                                            bottom = if (gridImages.indexOf(url) < 2) 1.dp else 0.dp
                                        ),
                                    contentScale = ContentScale.Crop
                                )

                                // Enhanced overlay for additional images
                                if (gridImages.indexOf(url) == 3 && images.size > 4) {
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.Black.copy(alpha = 0.7f)
                                                    )
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "+${images.size - 4}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 24.sp
                                            )
                                            Text(
                                                text = "more",
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernActionBar(
    isLiked: Boolean,
    isSaved: Boolean,
    likeCount: Int,
    commentCount: Int,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Like button with animation
            IconButton(
                onClick = onLikeClick,
                modifier = Modifier.size(40.dp)
            ) {
                AnimatedVisibility(
                    visible = isLiked,
                    enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                    exit = scaleOut()
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Unlike",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
                AnimatedVisibility(
                    visible = !isLiked,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                text = if (isLiked) "${likeCount + 1}" else likeCount.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(end = 16.dp)
            )

            IconButton(
                onClick = onCommentClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = commentCount.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(end = 16.dp)
            )

            IconButton(
                onClick = onShareClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Save button
            IconButton(
                onClick = onSaveClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = if (isSaved) "Unsave" else "Save",
                    tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Divider(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            thickness = 0.5.dp
        )
    }
}

@Composable
fun ModernFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = modifier
            .size(56.dp)
            .scale(scale)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                ),
                shape = CircleShape
            )
            .clickable {
                isPressed = true
                onClick()
            }
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Create Post",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ModernLoadMoreIndicator(onLoadMore: () -> Unit) {
    LaunchedEffect(Unit) {
        onLoadMore()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✨",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Loading more amazing posts...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
