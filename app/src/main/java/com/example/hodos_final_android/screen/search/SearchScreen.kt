package com.example.hodos_final_android.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.CustomPullRefreshIndicator
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.helper.rememberDebouncedState
import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationLocation
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.view_model.LocationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(
    viewModel: LocationViewModel = hiltViewModel()
) {
    val paginationState by viewModel.paginationState.collectAsState()
    val navController = LocalNavController.current
    var searchQuery by remember { mutableStateOf("") }
    var isLoadingMore by remember { mutableStateOf(false) }
    val isRefreshing = paginationState.isLoading && paginationState.data != null
    // Debounce search query
    val debouncedSearchQuery by rememberDebouncedState(searchQuery, debounceMillis = 500)


    val isFetched = remember { mutableStateOf(true) }

    // Add pull-to-refresh state
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() =
        refreshScope.launch {
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
            delay(1500)
            isFetched.value = true
            refreshing = false
        }

    val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)
    // Call API on search query change
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
    }

    Box{
        Column(modifier = Modifier.fillMaxSize().padding(WindowInsets.statusBars.asPaddingValues()).background(MaterialTheme.colorScheme.secondary)) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { query ->
                    searchQuery = query
                },
                onBack = {
                    navController.popBackStack()
                }
            )

            CustomPullRefreshIndicator(refreshing = refreshing)

            when {
                paginationState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Lỗi: ${paginationState.error?.message ?: "Không rõ lỗi"}")
                    }
                }

                else -> {
                    val locations = paginationState.data?.data ?: emptyList()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState),
                        ) {
                        items(locations) { location ->
                            LocationItem(data = location, onClick = {
                                navController.navigateWithAnimation(Screen.LocationDetailScreen.createRoute(location.id))
                            })
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

                                // Trigger load more
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


        }

        if(paginationState.isLoading ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Loading()
            }
        }
    }


}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp)
    ) {
        Row {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Filters",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { /* Open filters */ }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size),
                            contentDescription = "Filters",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun LocationItem(
    data: Location,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hotel Image
            GlideImage(
                model = data.img,
                contentDescription = data.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            ) {
                it.placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
            }

            // Hotel Details
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                data?.name?.let {
                    Text(
                        text = it,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location",
                        tint = Color(0xFF0D6EFD),
                        modifier = Modifier.size(16.dp)
                    )

                    data?.address?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

            }
        }
    }
}
