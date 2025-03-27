package com.example.hodos_final_android.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.hodos_final_android.LocalNavController

data class Hotel(
    val id: String,
    val name: String,
    val location: String,
    val price: String,
    val rating: Float,
    val reviews: String,
    val imageUrl: String
)


@Composable
fun SearchScreen() {
    val navController = LocalNavController.current
    var searchQuery by remember { mutableStateOf("") }

    val hotels = remember {
        listOf(
            Hotel(
                id = "1",
                name = "Landmark Hotel",
                location = "Victoria Island Lagos",
                price = "50,000",
                rating = 4.9f,
                reviews = "3k",
                imageUrl = "https://vielimousine.com/wp-content/uploads/2023/11/bai-sau-vung-tau.jpg"
            ),
            Hotel(
                id = "2",
                name = "Oriental Hotel",
                location = "Victoria Island Lagos",
                price = "60,000",
                rating = 4.9f,
                reviews = "3k",
                imageUrl = "https://vielimousine.com/wp-content/uploads/2023/11/bai-sau-vung-tau.jpg"
            ),
            Hotel(
                id = "3",
                name = "Four Point Hotel",
                location = "Victoria Island Lagos",
                price = "50,000",
                rating = 4.9f,
                reviews = "3k",
                imageUrl = "https://vielimousine.com/wp-content/uploads/2023/11/bai-sau-vung-tau.jpg"
            ),
            Hotel(
                id = "4",
                name = "Eko Hotel",
                location = "Victoria Island Lagos",
                price = "50,000",
                rating = 4.9f,
                reviews = "3k",
                imageUrl = "https://vielimousine.com/wp-content/uploads/2023/11/bai-sau-vung-tau.jpg"
            ),
            Hotel(
                id = "5",
                name = "Eko Hotel",
                location = "Victoria Island Lagos",
                price = "50,000",
                rating = 4.9f,
                reviews = "3k",
                imageUrl = "https://vielimousine.com/wp-content/uploads/2023/11/bai-sau-vung-tau.jpg"
            ),
            Hotel(
                id = "7",
                name = "Eko Hotel",
                location = "Victoria Island Lagos",
                price = "50,000",
                rating = 4.9f,
                reviews = "3k",
                imageUrl = "https://vielimousine.com/wp-content/uploads/2023/11/bai-sau-vung-tau.jpg"
            ),
            Hotel(
                id = "6",
                name = "Eko Hotel",
                location = "Victoria Island Lagos",
                price = "50,000",
                rating = 4.9f,
                reviews = "3k",
                imageUrl = "https://vielimousine.com/wp-content/uploads/2023/11/bai-sau-vung-tau.jpg"
            )
        )
    }

    val filteredHotels = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            hotels
        } else {
            hotels.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.location.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        // Search Bar
        HotelSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        // Hotel List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredHotels) { hotel ->
                HotelItem(
                    hotel = hotel,
                    onClick = {
                        // Navigate to hotel details
                    }
                )
            }
        }
    }
}

@Composable
fun HotelSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp)
    ) {
        Row {
            IconButton(onClick = { /* Open filters */ }) {
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
                            tint = Color(0xFF0D6EFD)
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HotelItem(
    hotel: Hotel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
                model = hotel.imageUrl,
                contentDescription = hotel.name,
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
                Text(
                    text = hotel.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

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

                    Text(
                        text = hotel.location,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Text(
                    text = "${hotel.price}/Night",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0D6EFD)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = "${hotel.rating}/${hotel.reviews} reviews",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

