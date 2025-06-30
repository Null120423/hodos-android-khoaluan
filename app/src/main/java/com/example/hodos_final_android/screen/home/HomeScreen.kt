package com.example.hodos_final_android.screen.home


import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.CarouselExample
import com.example.hodos_final_android.component.ColumnCenter
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.component.NewItem
import com.example.hodos_final_android.component.RowBetween
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.SkeletonList
import com.example.hodos_final_android.component.Title
import com.example.hodos_final_android.component.TravelCard
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.di.HomeViewModelEntryPoint
import com.example.hodos_final_android.di.UserViewModelEntryPoint
import com.example.hodos_final_android.helper.TokenManager
import com.example.hodos_final_android.helper.getScreenHeight
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.model.Category
import com.example.hodos_final_android.model.GetUserInfoModel
import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.model.New
import com.example.hodos_final_android.model.categories
import com.example.hodos_final_android.model.news
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.view_model.AuthViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }

    val homeViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, HomeViewModelEntryPoint::class.java)
            .homeViewModel()
    }

    val authState by userViewModel.authState.collectAsState()
    val homeState by homeViewModel.homeState.collectAsState()
    val navController = LocalNavController.current


    // Add pull-to-refresh state
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() =
        refreshScope.launch {
            refreshing = true
            homeViewModel.fetchTop10Locations()
            delay(1500)
            refreshing = false
        }

    val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)


    LaunchedEffect(authState) {

        val accessToken = authState?.accessToken

        if (accessToken == null && homeState.isLoading) {
            val tokenManager = TokenManager.getInstance()
            val getUserInfoModel = tokenManager.getAccessToken()?.let {
                GetUserInfoModel(
                    accessToken = it,
                    refreshToken = tokenManager.getRefreshToken()!!
                )
            }
            if (getUserInfoModel != null) {
                authViewModel.userInfo(getUserInfoModel)
            }
        }

    }

    LaunchedEffect(homeState) {
        if(homeState.data == null && homeState.isLoading)  {
            homeViewModel.fetchTop10Locations()
            delay(1500)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    stickyHeader {
                        Box(modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(horizontal = 15.dp)
                            .padding(WindowInsets.statusBars.asPaddingValues())){
                            RowBetween{
                                Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                    .weight(0.8f)
                                    .clickable {
                                        navController.navigateWithAnimation(Screen.SearchScreen.route)
                                    },
                                shape = RoundedCornerShape(100.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                colors = CardDefaults.outlinedCardColors(
                                    containerColor = MaterialTheme.colorScheme.background
,
                                        contentColor = MaterialTheme.colorScheme.tertiary
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 12.dp), // Optional: inner spacing
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "Search",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Txt(
                                           value = "Search",
                                            maxLines = 1,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    }
                                }



                                IconButton(
                                    modifier = Modifier.weight(0.1f),
                                    onClick = {navController.navigateWithAnimation(Screen.NotificationScreen.route) }) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notifications",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }


                            }
                        }
                    }
                    item {
                        FeatureIconsRow()
                    }

                    item {
                        Column(modifier = Modifier.heightIn(min = getScreenHeight().dp)
                            .background(MaterialTheme.colorScheme.secondary)
                            ,
                        ){
                            ColumnCenter(modifier = Modifier
                                .padding(top = 0.dp, bottom = 10.dp )){

                                // skeleton for home page
                                if(homeState.isLoading  ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                            .placeholder(
                                                visible = true,
                                                highlight = PlaceholderHighlight.shimmer(),
                                                color = Color.LightGray
                                            )
                                    )
                                    Seprate( height = 10)
                                    CategoriesView(categories)
                                    SkeletonList(title = "Locations for you")
                                    SkeletonList(title = "Foods for you")
                                    SkeletonList(title = "Post helpful!")
                                }

                                CarouselExample(rounded = 0, height = 220, banners = if(homeState.data != null) homeState.data!!.banners else  emptyList())
                                Seprate( height = 10)

                                if(!homeState.isLoading) {
                                    CategoriesView(categories)
                                }
                                if(homeState.data != null) {
                                    LocationCardGrid(locations = homeState.data!!.locationData.lst)
                                    FoodCardGrid(locations = homeState.data!!.foodData.lst)
                                    NewList(news = news)
                                }

                                Seprate(height = 100)

                            }
                        }
                    }
                }

                PullRefreshIndicator(refreshing,
                    pullRefreshState,
                    Modifier.align(Alignment.TopCenter),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.primary
                    )
            }

    }
}

@Composable
fun LocationCardGrid(
    locations: List<Location>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Seprate(height = 10)
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Title(value = "Locations for you", size = 16, fontWeight = FontWeight.Bold)
            Txt(value = "", size = 12)
        }

        Seprate(height = 10)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(locations) { location ->
                TravelCard(data = location)
            }
        }
    }
}


@Composable
fun NewList(
    news: List<New>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Seprate(height = 10)
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Title(value = "Post helpful!", size = 16, fontWeight = FontWeight.Bold)
        }

        Seprate(height = 10)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(news) { new ->
                NewItem(data = new)
            }
        }
    }
}



@Composable
fun FoodCardGrid(
    locations: List<Location>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Seprate(height = 10)
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Title(value = "Foods for you", size = 16, fontWeight = FontWeight.Bold)
            Txt(value = "", size = 12)
        }

        Seprate(height = 10)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(locations) { location ->
                TravelCard(data = location)
            }
        }
    }
}



@Composable
fun CategoriesView(
    categories: List<Category>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Seprate(height = 10)
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Title(value = "Categories", size = 16, fontWeight = FontWeight.Bold)
            Txt(value = "", size = 12)
        }

        Seprate(height = 10)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(categories) { cate ->
                CategoryHomeItem(cate)
            }
        }
    }
}

@Composable
fun CategoryHomeItem(
    category: Category,
    modifier: Modifier = Modifier
) {
    val navController = LocalNavController.current
    Card(
        onClick = {
            navController.navigateWithAnimation(Screen.ComingSoonScreen.route)
        },
        modifier = modifier
            .height(60.dp)
            .widthIn(min = (getScreenWidth()/2 -30 ).dp)
            .clip(RoundedCornerShape(1000.dp))
            .clickable { /* TODO: handle click */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 10.dp).fillMaxWidth()
        ) {
            ImgWithUrl(
                url = category.thumbnail,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Seprate(width = 25)

            Txt(
                value = category.title
            )
        }
    }
}

@Composable
fun FeatureIconsRow() {
    val navController = LocalNavController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FeatureItem(R.drawable.classcifical_feature, "Classical", {
            navController.navigateWithAnimation(Screen.PredictScreen.route)
        })
        FeatureItem(R.drawable.planning_fea, "Planning" , {
            navController.navigateWithAnimation(Screen.Planning.route)
        })
        FeatureItem(R.drawable.chat_ai_fea, "Assistant", {
            navController.navigateWithAnimation(Screen.ChatAiDashBoard.route)
        })
        FeatureItem(R.drawable.ar, "Assistant", {
            navController.navigateWithAnimation(Screen.TripDirectionScreen.route)
        })
        FeatureItem(R.drawable.more_feature, "More", {
            navController.navigateWithAnimation(Screen.ComingSoonScreen.route)
        })
    }
}



@Composable
fun FeatureItem(iconRes: Int, title: String,onClick :   () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        IconButton(onClick =onClick, modifier = Modifier.size(50.dp)) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(50.dp),
            )
        }
    }

}

