package com.hodos.hodos_final_android.screen.notification

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hodos.hodos_final_android.LocalNavController
import com.hodos.hodos_final_android.Screen
import com.hodos.hodos_final_android.component.EmptyStateWithAnimation
import com.hodos.hodos_final_android.component.Loading
import com.hodos.hodos_final_android.component.Seprate
import com.hodos.hodos_final_android.di.NotificationViewEntryPoint
import com.hodos.hodos_final_android.di.UserViewModelEntryPoint
import com.hodos.hodos_final_android.helper.rememberDebouncedState
import com.hodos.hodos_final_android.model.NotificationModel
import com.hodos.hodos_final_android.model.Pagination
import com.hodos.hodos_final_android.model.PaginationLocation
import com.hodos.hodos_final_android.navigateWithAnimation
import com.hodos.hodos_final_android.screen.RequireLoginScreen
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NotificationData(
    val id: String,
    val title: String,
    val message: String,
    val type: String,
    val isRead: Boolean,
    val createdAt: String,
    val typeData: NotificationTypeData
)

data class NotificationTypeData(
    val color: String,
    val name: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NotificationScreen() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val notificationViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, NotificationViewEntryPoint::class.java)
            .notificationViewModel()
    }

    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }


    val paginationState by notificationViewModel.notificationPaginationState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isLoadingMore by remember { mutableStateOf(false) }
    val debouncedSearchQuery by rememberDebouncedState(searchQuery, debounceMillis = 500)
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        notificationViewModel.pagination(
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


    val total = paginationState.data?.total

    val unreadTotal = paginationState.data?.unreadCount

    val isLogin = userViewModel.authState.collectAsState().value?.user != null


// Trigger search when query changes
    LaunchedEffect(debouncedSearchQuery) {
        if (!isLogin) return@LaunchedEffect
        notificationViewModel.pagination(
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Notifications",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (unreadTotal != null) {
                            if (unreadTotal > 0) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Badge(
                                    containerColor = Color(0xFFFF5722)
                                ) {
                                    Text(
                                        text = unreadTotal.toString(),
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (!isLogin) PaddingValues(0.dp) else paddingValues)
                .background(MaterialTheme.colorScheme.secondary)
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when {
                    !isLogin -> {
                        Seprate(height = 20)
                        RequireLoginScreen()
                    }
                    paginationState.error != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Lỗi: ${paginationState.error?.message ?: "Không rõ lỗi"}")
                        }
                    }
                    else -> {
                        val notifications = paginationState.data?.data ?: emptyList()

                        if(notifications.isEmpty()) {
                            EmptyStateWithAnimation()
                        }
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 10.dp)
                        ) {
                            items(notifications) { noti ->
                                NotificationCard(
                                    notification = noti,
                                )
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
                                            notificationViewModel.pagination(
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

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.secondary
            )

            if (paginationState.isLoading && paginationState.data == null && isLogin) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Loading()
                }
            }
        }

    }


}

@Composable
fun NotificationCard(notification: NotificationModel) {
    val navController = LocalNavController.current
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(bottom = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("id", notification.id)
            navController.navigateWithAnimation(Screen.NotificationDetailScreen.route)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Notification Icon
            val typeData = notification.typeData
            if (typeData != null) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(typeData.color)).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getNotificationIcon(notification.type),
                        contentDescription = notification.type,
                        tint = Color(android.graphics.Color.parseColor(typeData.color)),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Notification Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2196F3))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Type Badge
                    if (typeData != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(android.graphics.Color.parseColor(typeData.color)).copy(alpha = 0.1f)
                        ) {
                            Text(
                                text =typeData.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(android.graphics.Color.parseColor(typeData.color)),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Timestamp
                    Text(
                        text = formatTimestamp(notification.createdAt),
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
            }
        }
    }
}

fun getNotificationIcon(type: String): ImageVector {
    return when (type) {
        "reminder" -> Icons.Default.Notifications
        "alert" -> Icons.Default.Warning
        else -> Icons.Default.Notifications
    }
}

fun formatTimestamp(timestamp: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        val date = inputFormat.parse(timestamp)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        "Just now"
    }
}
