package com.example.hodos_final_android.screen.planing

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.Title
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.di.PlanTripModelEntryPoint
import com.example.hodos_final_android.di.UserViewModelEntryPoint
import com.example.hodos_final_android.helper.rememberDebouncedState
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationLocation
import com.example.hodos_final_android.model.Trip
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.screen.RequireLoginScreen
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TripUserScreen() {
    val context = LocalContext.current
    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }

    val planTripViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, PlanTripModelEntryPoint::class.java)
            .planTripModel()
    }

    val paginationTripUserState by planTripViewModel.paginationTripUserState.collectAsState()

    val navController = LocalNavController.current
    var searchQuery by remember { mutableStateOf("") }
    var isLoadingMore by remember { mutableStateOf(false) }
    val debouncedSearchQuery by rememberDebouncedState(searchQuery, debounceMillis = 500)
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        planTripViewModel.paginationTripUser(
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

    fun handleNavTripDetail(id: String)  {
        navController.navigateWithAnimation(
            Screen.TripDetailScreen.createRoute(id)
        )
    }


    // Trigger search when query changes
    LaunchedEffect(debouncedSearchQuery) {
        planTripViewModel.paginationTripUser(
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

    if(userViewModel.getAccessToken() == null ) {
        RequireLoginScreen()
    }else {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
                .pullRefresh(pullRefreshState)
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Seprate(height = 40)
                }
                val trips = paginationTripUserState.data?.data ?: emptyList()

                items(trips) { trip ->
                    RegularTripCard(trip = trip, onDetail = {
                        trip.id?.let { handleNavTripDetail(it) }
                    })
                }
                if (paginationTripUserState.data?.hasNext == true) {
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
                                planTripViewModel.paginationTripUser(
                                    Pagination(
                                        skip = paginationTripUserState.data?.nextSkip ?: 0,
                                        take = paginationTripUserState.data?.take ?: 20,
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
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.secondary
            )

            if (paginationTripUserState.isLoading && paginationTripUserState.data == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Loading(title = "")
                }
            }
            if(paginationTripUserState.error != null ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Title(
                        value = paginationTripUserState.error!!.message
                    )
                }
            }
        }
    }


}


@Composable
fun RegularTripCard(trip: Trip, onDetail: () -> Unit?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                trip.id?.let { onDetail() }
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            trip?.thumbnail?.let {
                ImgWithUrl(
                    url = it,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF388E3C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Txt(
                            value = "t",
                            color = MaterialTheme.colorScheme.background,
                            fontWeight = FontWeight.Bold,
                            size = 12
                        )
                    }

                    Seprate(width = 10)
                    Txt(
                        value = trip.typeTrip.capitalize(),
                        size = 16,
                        fontWeight = FontWeight.Bold
                    )
                }



                Txt(
                    value = trip.startDate + " - " + trip.endDate,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu_day),
                        contentDescription = "Travel type",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Txt(
                        value = "Total Day : " + trip.totalDays.toString(),
                    )

                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_media_rew),
                        contentDescription = "Travel type",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Txt(
                        value = "Budget" + " : " + trip.budget,
                    )

                }

            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Handle more options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }

            }
        }
    }
}

