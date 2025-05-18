package com.example.hodos_final_android.screen.post

import PostDetailDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Send
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
import com.example.hodos_final_android.component.ColumnStart
import com.example.hodos_final_android.component.EmptyView
import com.example.hodos_final_android.component.HighlightedContent
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.component.ProfileAvatar
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.Title
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.di.PostViewModelEntryPoint
import com.example.hodos_final_android.helper.getScreenWidth
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostScreen(
) {
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

    // Sample data with URLs
    val storyItems = remember {
        listOf(
            StoryItem("1", "Kate Mary", "https://cdn.dribbble.com/users/4137552/avatars/normal/d151a2d9509323de835e33009de57589.png?1684156519", true),
            StoryItem("2", "Jacki Hall", "https://cdn.dribbble.com/users/2616092/avatars/normal/bafec5669a661504f172a0813d464139.jpg?1721321274", true),
            StoryItem("3", "Amy Adam", "https://cdn.dribbble.com/users/3112201/avatars/normal/bd7fe692d89ca29944a4549bab12e17a.jpg?1628089893", false),
            StoryItem("4", "James Love", "https://cdn.dribbble.com/users/21506638/avatars/normal/dbb5eb345db100e6fa63dfd173d3c31f.png?1728071059", false),
            StoryItem("5", "Your Story", "https://cdn.dribbble.com/users/1565678/avatars/normal/f7141e584986ea624b56d2eaada3e330.jpg?1643382230", false)
        )
    }

    LaunchedEffect(Unit) {
        if(paginationPostState.data == null ) {
            postViewModel.pagination(
                Pagination(
                    skip = 0,
                    take = 20,
                    where = {

                    }
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
            EmptyView(title = "No Post yet!")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Seprate(height = 50)
                }
                item {
                    StoriesSection(storyItems)
                }

                items(posts) { post ->
                    PostCard(post = post,
                        onPostClick = { selectedPost = post })
                }
                if (paginationPostState.data?.hasNext == true) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Load more...")
                        }

                        LaunchedEffect(Unit) {
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

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 10.dp)
        ) {
            IconButton(
                onClick = {
                    navController.navigateWithAnimation(Screen.CreatePostScreen.route)
                },
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Story",
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.secondary
        )

        if (paginationPostState.isLoading && paginationPostState.data == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Loading(title = "")
            }
        }
        if(paginationPostState.error != null ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Title(
                    value = paginationPostState.error!!.message
                )
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
fun StoriesSection(stories: List<StoryItem>) {
//    LazyRow(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        contentPadding = PaddingValues(horizontal = 8.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(stories) { story ->
//            StoryItem(story)
//        }
//    }

    Divider(
        modifier = Modifier.padding(top = 8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    )
}

@Composable
fun StoryItem(story: StoryItem) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            // Colored ring for unseen stories
            if (story.hasUnseenStory) {
                Box(
                    modifier = Modifier
                        .size(65.dp)
                        .clip(CircleShape)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary
                                )
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.background,
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
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
                contentDescription = "Profile of",
                modifier = Modifier
                    .size(if (story.hasUnseenStory) 56.dp else 60.dp)
                    .clip(CircleShape)
                    .border(
                        width = if (story.hasUnseenStory) 0.dp else 2.dp,
                        color = if (story.hasUnseenStory) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }

        Text(
            text = story?.username ?: "",
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }


}

@Composable
fun PostCard(post: Post, onPostClick : () -> Unit) {
    val navController = LocalNavController.current
    fun handleClickLabel(label: String) {
        val labelNotTag = label.substring(1)
        val locationByTag = post.locations.find { it -> it.label == labelNotTag }

        if(locationByTag != null ){
            navController.navigateWithAnimation(Screen.LocationDetailScreen.createRoute(locationByTag.id))
        }

    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background) ,
        onClick = {
            onPostClick()
        },
        shape = RectangleShape
    ) {
        // Post header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAvatar(
                letter = post?.username?.firstOrNull()?.uppercase() ?: "?",
                backgroundColor = Color(0xFF4CAF50),
                modifier = Modifier.size(32.dp)
            )

            ColumnStart(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp)
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

            IconButton(onClick = { /* TODO: Show options menu */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }
        }

        // Caption
        Box(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            post?.content?.let {
                HighlightedContent(text = it) { tag ->
                    // Xử lý khi bấm vào hashtag
                    println("Clicked tag: $tag")
                    handleClickLabel(tag)
                }
            }
        }

        Seprate(height = 10)


        // Post content
        // Post content
        // Post content
        if (post.imgs.size == 1) {
            ImgWithUrl(
                url = post.imgs.first(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    , // Vuông
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                when (post.imgs.size) {
                    2 -> {
                        Row(Modifier.height(180.dp)) {
                            post.imgs.take(2).forEach { url ->
                                ImgWithUrl(
                                    url = url,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(200.dp)
                                )
                            }
                        }
                    }

                    3 -> {
                        Row(Modifier.height(180.dp)) {
                            post.imgs.take(2).forEach { url ->
                                ImgWithUrl(
                                    url = url,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(190.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        ImgWithUrl(
                            url = post.imgs[2],
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .width(getScreenWidth().dp)
                                .height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        val gridImages = post.imgs.take(4)
                        for (row in gridImages.chunked(2)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            ) {
                                for ((index, url) in row.withIndex()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                    ) {
                                        ImgWithUrl(
                                            url = url,
                                            modifier = Modifier
                                                .fillMaxSize()
                                        )

                                        // Overlay nếu là ảnh cuối cùng + có thêm
                                        if (gridImages.indexOf(url) == 3 && post.imgs.size > 4) {
                                            Box(
                                                modifier = Modifier
                                                    .matchParentSize()
                                                    .background(Color.Black.copy(alpha = 0.5f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "+${post.imgs.size - 4}",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 22.sp
                                                )
                                            }
                                        }
                                    }
                                }

                                // Nếu chỉ có 1 ảnh trong dòng -> Spacer
                                if (row.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* TODO: Like action */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Like",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = post.timePosted.toString(),
                modifier = Modifier.padding(end = 16.dp),
                fontSize = 14.sp
            )

            IconButton(
                onClick = { /* TODO: Comment action */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comment"
                )
            }

            Text(
                text = post.commentCount.toString(),
                modifier = Modifier.padding(end = 16.dp),
                fontSize = 14.sp
            )

            IconButton(
                onClick = { /* TODO: Share action */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Share"
                )
            }
        }

        Divider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))



    }

}