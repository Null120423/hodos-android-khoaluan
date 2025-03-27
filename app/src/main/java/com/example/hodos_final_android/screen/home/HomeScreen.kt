package com.example.hodos_final_android.screen.home


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.AuthButtons
import com.example.hodos_final_android.component.FloatingActionGroup
import com.example.hodos_final_android.component.InfoDialog
import com.example.hodos_final_android.component.TravelCard
import com.example.hodos_final_android.mock.Locations
import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.theme.HodosTheme

@OptIn(ExperimentalFoundationApi::class)
@Preview()
@Composable
fun HomeScreen() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val navController = LocalNavController.current
    val locations = Locations.fromJson()

    HodosTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.home_banner),
                    contentDescription = "Home Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )

            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                // Header Home không có search bar
                stickyHeader{
                   Box(
                       modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(20.dp)
                   ) {
                       AuthButtons(
                           onRegisterClick = {
                               navController.navigateWithAnimation(Screen.Register.route)
                           },
                           onSignInClick = {
                               navController.navigateWithAnimation(Screen.Login.route)
                           }
                       )
                   }
                }
                item {
                    Box(modifier = Modifier.padding(horizontal = 10.dp)){
                        HeaderHomeWithoutSearch()
                    }

                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 10.dp)){
                        SearchBar(onClick = {
                            navController.navigateWithAnimation(Screen.SearchScreen.route)
                        })
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 10.dp)){
                        FeatureIconsRow()
                    }
                }

                // Phần nội dung cuộn tiếp tục bình thường
                item {
                    Box(modifier = Modifier.padding(horizontal = 10.dp)){
                        TravelCardGrid(locations = locations)
                    }

                }
            }

            FloatingActionGroup()

        }


    }
}

@Composable
fun TravelCardGrid(
    locations: List<Location>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 0.dp, end = 0.dp)
    ) {
        for (i in locations.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TravelCard(data = locations[i]) // Phần tử đầu tiên

                if (i + 1 < locations.size) {
                    TravelCard(data = locations[i + 1]) // Phần tử tiếp theo nếu tồn tại
                } else {
                    Spacer(modifier = Modifier.weight(1f)) // Giữ khoảng trống nếu số phần tử lẻ
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // Khoảng cách giữa các hàng
        }
    }
}




@Composable
fun HeaderHomeWithoutSearch() {
    val infoDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        // Location & Notification
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LocationCard(onClick = {
                infoDialog.value = true
            } )
            NotificationButton()
        }
        Spacer(modifier = Modifier.height(10.dp))


        // Main Text
        Text(
            text = "Explore the Beautiful Places",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 40.sp,
        )

        Spacer(modifier = Modifier.height(10.dp))


    }

    if (infoDialog.value) {
        InfoDialog(
            title = "Turn on Location Service",
            desc = "Explore the world without getting lost and keep the track of your location.",
            onDismiss = {
                infoDialog.value = false
            }
        )
    }
}

@Composable
fun LocationCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clip(CircleShape)
            .clickable { /* Handle location click */ },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = CircleShape,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.location_icon),
                contentDescription = "Location",
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Text(
                text = "TP. Hồ Chí Minh",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun NotificationButton() {
    IconButton(
        onClick = { /* Handle notification click */ },
        modifier = Modifier.size(52.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.bell_icon),
            contentDescription = "Notifications",
            modifier = Modifier.size(30.dp),
            tint = Color.Unspecified
        )
    }
}
@Composable
fun FeatureIconsRow() {
    val navController = LocalNavController.current


    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        shape = RoundedCornerShape(1000.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
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
            FeatureItem(R.drawable.more_feature, "More", {})
        }
    }
}



@Composable
fun FeatureItem(iconRes: Int, title: String,onClick :   () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        IconButton(onClick =onClick, modifier = Modifier.size(60.dp)) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(60.dp),
            )
        }
    }

}

