package com.example.hodos_final_android.screen.blog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.EmptyBlogWithAnimation
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.di.BlogViewEntryPoint
import com.example.hodos_final_android.helper.rememberDebouncedState
import com.example.hodos_final_android.model.BlogModel
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationLocation
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.screen.search.SearchBar
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class BlogPost(
    val id: String,
    val title: String,
    val thumbnail: String,
    val tag: String,
    val content: String,
    val createdAt: String,
    val isPublish: Boolean
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun BlogListScreen() {
    val context = LocalContext.current
    val viewModel = remember {
        EntryPointAccessors
            .fromApplication(context, BlogViewEntryPoint::class.java)
            .blogViewModel()
    }

    val paginationState by viewModel.blogPaginationState.collectAsState()
    val navController = LocalNavController.current
    var searchQuery by remember { mutableStateOf("") }
    var isLoadingMore by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val debouncedSearchQuery by rememberDebouncedState(searchQuery, debounceMillis = 500)
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        viewModel.pagination(
            Pagination(
                skip = 0,
                take = 20,
                where = PaginationLocation(
                    name = debouncedSearchQuery,
                    type = ""
                )
            )
        )
        delay(500)
        refreshing = false
    }

    val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)


    // Trigger search when query changes
    LaunchedEffect(debouncedSearchQuery) {
        viewModel.pagination(
            Pagination(
                skip = 0,
                take = 20,
                where = PaginationLocation(
                    name = debouncedSearchQuery,
                    type = ""
                )
            )
        )
        isLoading = false
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Blog Posts",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                paginationState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${paginationState.error?.message ?: "Unknow error"}")
                    }
                }

                else -> {
                    val blogs = paginationState.data?.data ?: emptyList()


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        stickyHeader {
                            SearchBar(
                                query = searchQuery,
                                onQueryChange = {
                                    searchQuery = it
                                    isLoading = true
                                },
                                onBack = { navController.popBackStack() },
                                isBack = false,
                                borderColor =  MaterialTheme.colorScheme.primary
                            )
                        }
                        items(blogs) { post ->
                            val handleDetail = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("url", "https://hodos-admin.gitlabserver.id.vn/blog-detail/"+post.id)
                                navController.navigateWithAnimation(Screen.BlogDetailScreen.route)
                            }
                            BlogPostCard(post = post, onBlogClick = handleDetail)
                        }

                        if(blogs.isEmpty() && !isLoading && !paginationState.isLoading) {
                           item {
                               EmptyBlogWithAnimation()
                           }
                        }

                        if (paginationState.data?.hasNext == true) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Đang tải thêm...")
                                }

                                LaunchedEffect(Unit) {
                                    if (!isLoadingMore) {
                                        isLoadingMore = true
                                        viewModel.pagination(
                                            Pagination(
                                                skip = paginationState.data?.nextSkip ?: 0,
                                                take = paginationState.data?.take ?: 20,
                                                where = PaginationLocation(
                                                    name = debouncedSearchQuery,
                                                    type = ""
                                                )
                                            )
                                        )
                                        isLoadingMore = false
                                    }
                                }
                            }
                        }

                    }
                }
            }


            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.secondary
            )

            if ((paginationState.isLoading && paginationState.data == null) || isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Loading()
                }
            }

        }

    }

}

@Composable
fun BlogPostCard(post: BlogModel, onBlogClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onBlogClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = post.thumbnail,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.tag,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }
    }
}
