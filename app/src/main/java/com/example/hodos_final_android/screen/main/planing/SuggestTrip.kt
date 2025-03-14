package com.example.hodos_final_android.screen.main.planing


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.hodos_final_android.LocalNavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Trip(
    val id: String,
    val title: String,
    val imageUrl: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val isBookmarked: Boolean = false
)

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestTripScreen(
) {
    val navController = LocalNavController.current

    val trips = remember {
        List(20) {
            Trip(
                id = "trip_$it",
                title = "VUNG TAU 2 NGÀY 1 ĐÊM",
                imageUrl = "https://maikatours.com/wp-content/uploads/2020/02/Vung-Tau-Vietnam-travel-guide.jpg",
                startDate = LocalDate.of(2025, 4, 12),
                endDate = LocalDate.of(2025, 4, 15)
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "SUGGEST YOUR TRIPS",
                        fontWeight = FontWeight.Bold
                    )
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
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)){
            TripGrid(trips = trips, onTripClick = {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripGrid(
    trips: List<Trip>,
    onTripClick: (Trip) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(trips.size) { index ->
            val trip = trips[index]
            TripCard(trip = trip, onClick = { onTripClick(trip) })
        }
    }
}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripCard(
    trip: Trip,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = contentColorFor(MaterialTheme.colorScheme.secondary)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column {
            // Image
            AsyncImage(
                model = trip.imageUrl,
                contentDescription = trip.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), // Giảm chiều cao để phù hợp với lưới
                contentScale = ContentScale.Crop
            )

            // Trip details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp) // Giảm padding để phù hợp với lưới
            ) {
                Text(
                    text = trip.title,
                    fontSize = 14.sp, // Giảm kích thước chữ
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = buildString {
                        append("FROM ")
                        append(trip.startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        append(" TO ")
                        append(trip.endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    },
                    fontSize = 10.sp, // Giảm kích thước chữ
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AsyncImage(
    model: String,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
) {
    GlideImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        alignment = alignment,
    )
}
