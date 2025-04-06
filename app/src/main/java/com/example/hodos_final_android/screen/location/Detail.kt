package com.example.hodos_final_android.screen.location

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Attractions
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Surfing
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.component.CarouselExample
import com.example.hodos_final_android.component.ColumnStart
import com.example.hodos_final_android.model.LocationDetail
import com.example.hodos_final_android.screen.main.planing.AsyncImage
import com.example.hodos_final_android.view_model.LocationDetailViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    navController: NavController,
    locationId: String? = null,
    viewModel: LocationDetailViewModel = hiltViewModel()
) {
    // In a real app, you would fetch the location details based on the locationId
    // For this example, we'll use a mock location
    val location = remember {
        LocationDetail(
            id = locationId ?: "1",
            name = "Crystal Wild Campsite",
            hotelName = "Lafayette, Ca",
            rating = 4,
            reviews = 72000,
            region = "Lafayette, Ca",
            distanceToCenter = 1,
            guests = 2,
            stayDuration = 10,
            amenities = listOf("1 king bed", "Free wi-fi", "TV"),
            pricePerNight = 35,
            imageResId = R.drawable.predict_bg // Replace with your image resource
        )
    }

    var isFavorite by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Main content with rounded corners
        Box {

            CoxsBazarBeachInfo()


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                // Background image


                // Top navigation buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Back button
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0x88000000), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    // Action buttons
                    Row {
                        IconButton(
                            onClick = { /* Share functionality */ },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0x88000000), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CoxsBazarBeachInfo() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        CarouselExample( height = 300)
        // Header section with beach name and location
        ColumnStart(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Cox's Bazar Beach",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    Icons.Filled.Place,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Cox's Bazar, Bangladesh",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp
                )
            }

            // Rating and visitors section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    ProfileImage("https://cdn.dribbble.com/userupload/17943328/file/original-2822e07d3e9307a770dad451b231fd3c.png?resize=1024x768&vertical=center")
                    Spacer(modifier = Modifier.width(4.dp))
                    ProfileImage("https://cdn.dribbble.com/userupload/17943328/file/original-2822e07d3e9307a770dad451b231fd3c.png?resize=1024x768&vertical=center")
                    Spacer(modifier = Modifier.width(4.dp))
                    ProfileImage("https://cdn.dribbble.com/userupload/17943328/file/original-2822e07d3e9307a770dad451b231fd3c.png?resize=1024x768&vertical=center")
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(
                            text = "2M+",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "4.9",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "/5",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            // Tab navigation
            var selectedTab by remember { mutableStateOf("Overview") }
            val tabs = listOf("Overview", "Details", "Reviews", "Location", "Weather")

            ScrollableTabRow(
                selectedTabIndex = tabs.indexOf(selectedTab),
                edgePadding = 0.dp,
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {},
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)])
                            .height(3.dp)
                            .padding(horizontal = 16.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                            )
                    )
                }
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = {
                            Text(
                                text = tab,
                                fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == tab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on selected tab
            when (selectedTab) {
                "Overview" -> OverviewContent()
                "Details" -> DetailsContent()
                "Reviews" -> ReviewsContent()
                "Location" -> LocationContent()
                "Weather" -> WeatherContent()
            }
        }
    }
}

@Composable
fun OverviewContent() {
    Column {
        // Quick info card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "World's Longest Natural Sea Beach",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Cox's Bazar is famous for its long natural sandy beach that stretches more than 120 kilometers, making it the longest natural sea beach in the world. The beach is the main attraction of the town, with a gentle slope perfect for swimming and various water activities.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Highlights section
        Text(
            text = "Highlights",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HighlightItem(
                icon = Icons.Filled.WaterDrop,
                title = "120+ km",
                subtitle = "Beach Length",
                modifier = Modifier.weight(1f)
            )
            HighlightItem(
                icon = Icons.Filled.WbSunny,
                title = "Nov-Mar",
                subtitle = "Best Time",
                modifier = Modifier.weight(1f)
            )
            HighlightItem(
                icon = Icons.Filled.Attractions,
                title = "15+",
                subtitle = "Attractions",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gallery section
        Text(
            text = "Gallery",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(6) { index ->
                GalleryImage(
                    url = "https://cdn.dribbble.com/userupload/17943328/file/original-2822e07d3e9307a770dad451b231fd3c.png?resize=1024x768&vertical=center",
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description section
        Text(
            text = "About",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Cox's Bazar is a town on the southeast coast of Bangladesh. It's known for its very long, sandy beachfront, stretching from Sea Beach in the north to Kolatoli Beach in the south. Aggameda Khyang monastery is home to bronze statues and centuries-old Buddhist manuscripts. South of town, the tropical rainforest of Himchari National Park has waterfalls and hiking trails. North, sea turtles breed on nearby Sonadia Island.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Activities section
        Text(
            text = "Popular Activities",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ActivityItem(
                icon = Icons.Filled.Surfing,
                title = "Surfing",
                description = "Enjoy surfing in the Bay of Bengal with moderate waves perfect for beginners and intermediate surfers."
            )
            ActivityItem(
                icon = Icons.Filled.DirectionsBoat,
                title = "Boat Tours",
                description = "Take a boat tour to nearby islands like Maheshkhali and Sonadia to explore their unique ecosystems."
            )
            ActivityItem(
                icon = Icons.Filled.Restaurant,
                title = "Seafood Dining",
                description = "Experience fresh seafood at beachside restaurants with spectacular ocean views."
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Call to action button
        Button(
            onClick = { /* Navigate to booking or more info */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Plan Your Visit",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun DetailsContent() {
    Column {
        // Location details
        InfoSection(
            title = "Location Details",
            content = "Cox's Bazar is located at 21.5833°N 92.0167°E in the southeastern corner of Bangladesh, with the Bay of Bengal to the west and Myanmar to the east."
        )

        // History
        InfoSection(
            title = "History",
            content = "Named after Captain Hiram Cox, who established a refugee settlement for Arakanese refugees in the late 18th century. The area has a rich cultural heritage influenced by Bengali and Rakhine traditions."
        )

        // Geography
        InfoSection(
            title = "Geography",
            content = "The beach slopes gently into the Bay of Bengal, making it safe for swimming. The area is surrounded by hills covered in lush greenery, creating a picturesque landscape."
        )

        // Climate
        InfoSection(
            title = "Climate",
            content = "Cox's Bazar has a tropical monsoon climate with a dry season from November to April and a rainy season from May to October. Average temperatures range from 15°C to 32°C throughout the year."
        )

        // Biodiversity
        InfoSection(
            title = "Biodiversity",
            content = "The region is home to diverse marine life and bird species. Nearby forests host various wildlife including monkeys, birds, and occasionally elephants in the deeper forest areas."
        )
    }
}

@Composable
fun ReviewsContent() {
    Column {
        // Overall rating
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "4.9",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row {
                    repeat(5) { index ->
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = if (index < 5) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Text(
                    text = "Based on 2,543 reviews",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sample reviews
        Text(
            text = "Recent Reviews",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        ReviewItem(
            name = "Sarah Johnson",
            rating = 5,
            date = "March 15, 2023",
            comment = "The beach is absolutely stunning! The longest natural sea beach I've ever seen. Crystal clear water and clean sand. Highly recommend visiting during sunrise."
        )

        ReviewItem(
            name = "Ahmed Hassan",
            rating = 4,
            date = "February 22, 2023",
            comment = "Beautiful place with amazing scenery. The beach stretches as far as the eye can see. Only giving 4 stars because some areas were a bit crowded during peak season."
        )

        ReviewItem(
            name = "Priya Sharma",
            rating = 5,
            date = "January 5, 2023",
            comment = "One of the most beautiful beaches in South Asia. The sunset view is breathtaking. Local seafood is delicious and fresh. Will definitely come back!"
        )

        // Write review button
        OutlinedButton(
            onClick = { /* Open review form */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Write a Review",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun LocationContent() {
    Column {
        // Map preview (placeholder)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            // In a real app, you would use Google Maps or another map provider here
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Filled.Map,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Map View",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "21.5833°N 92.0167°E",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Address and directions
        Text(
            text = "Address",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Cox's Bazar Beach\nCox's Bazar District\nChittagong Division\nBangladesh",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // How to get there
        Text(
            text = "How to Get There",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        TransportOption(
            icon = Icons.Filled.Flight,
            title = "By Air",
            description = "Cox's Bazar Airport (CXB) is located about 7 km from the beach. Regular flights from Dhaka take approximately 55 minutes."
        )

        TransportOption(
            icon = Icons.Filled.DirectionsBus,
            title = "By Bus",
            description = "Regular bus services connect Cox's Bazar with major cities including Dhaka, Chittagong, and Sylhet. The journey from Dhaka takes about 10-12 hours."
        )

        TransportOption(
            icon = Icons.Filled.Train,
            title = "By Train",
            description = "Take a train to Chittagong and then a bus or car to Cox's Bazar (approximately 2 hours drive from Chittagong)."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nearby attractions
        Text(
            text = "Nearby Attractions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        NearbyAttraction(
            name = "Himchari National Park",
            distance = "5 km",
            description = "A national park with beautiful waterfalls and hiking trails."
        )

        NearbyAttraction(
            name = "Inani Beach",
            distance = "23 km",
            description = "Known for its unique coral boulders and clear blue waters."
        )

        NearbyAttraction(
            name = "Maheshkhali Island",
            distance = "15 km by boat",
            description = "Famous for its Buddhist temples and panoramic views."
        )
    }
}

@Composable
fun WeatherContent() {
    Column {
        // Current weather
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Current Weather",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.WbSunny,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "28°C",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Sunny",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Feels like 30°C",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Column(horizontalAlignment = Alignment.End) {
                        WeatherDetail(label = "Humidity", value = "65%")
                        WeatherDetail(label = "Wind", value = "12 km/h")
                        WeatherDetail(label = "UV Index", value = "High")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weather forecast
        Text(
            text = "5-Day Forecast",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                ForecastDay(day = "Today", icon = Icons.Filled.WbSunny, high = "28°C", low = "23°C")
            }
            item {
                ForecastDay(day = "Wed", icon = Icons.Filled.Cloud, high = "27°C", low = "22°C")
            }
            item {
                ForecastDay(day = "Thu", icon = Icons.Filled.WbCloudy, high = "26°C", low = "22°C")
            }
            item {
                ForecastDay(day = "Fri", icon = Icons.Filled.Thunderstorm, high = "25°C", low = "21°C")
            }
            item {
                ForecastDay(day = "Sat", icon = Icons.Filled.WbSunny, high = "27°C", low = "22°C")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Best time to visit
        Text(
            text = "Best Time to Visit",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "The best time to visit Cox's Bazar is from November to March when the weather is dry and pleasant. The temperature ranges from 15°C to 32°C during this period, making it ideal for beach activities and sightseeing.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Seasonal information
        Text(
            text = "Seasonal Information",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        SeasonInfo(
            season = "Winter (Nov-Feb)",
            description = "Mild temperatures, clear skies, and low humidity make this the peak tourist season.",
            recommendation = "Ideal for all beach activities and sightseeing."
        )

        SeasonInfo(
            season = "Spring (Mar-Apr)",
            description = "Temperatures begin to rise, but still comfortable with occasional light showers.",
            recommendation = "Good for beach activities and exploring nearby attractions."
        )

        SeasonInfo(
            season = "Summer (May-Aug)",
            description = "Hot and humid with heavy rainfall during the monsoon season.",
            recommendation = "Not recommended for beach activities, but hotel rates are lower."
        )

        SeasonInfo(
            season = "Autumn (Sep-Oct)",
            description = "Rainfall decreases and temperatures become more moderate.",
            recommendation = "Good for budget travelers as it's just before peak season."
        )
    }
}

// Helper components

@Composable
fun HighlightItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ActivityItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InfoSection(
    title: String,
    content: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))
        Divider()
    }
}

@Composable
fun ReviewItem(
    name: String,
    rating: Int,
    date: String,
    comment: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Profile image placeholder
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = name.first().toString(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        repeat(5) { index ->
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = if (index < rating) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun TransportOption(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NearbyAttraction(
    name: String,
    distance: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

           Chip(
                onClick = { /* Navigate to attraction details */ },
              colors = ChipDefaults.chipColors(
                  backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
             Text(text = distance)
           }
        }
    }
}

@Composable
fun WeatherDetail(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ForecastDay(
    day: String,
    icon: ImageVector,
    high: String,
    low: String
) {
    Card(
        modifier = Modifier.width(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = day,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Icon(
                icon,
                contentDescription = null,
                tint = when (icon) {
                    Icons.Filled.WbSunny -> Color(0xFFFFC107)
                    Icons.Filled.Thunderstorm -> Color(0xFF2196F3)
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = high,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = low,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SeasonInfo(
    season: String,
    description: String,
    recommendation: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = season,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Recommend,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = recommendation,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ProfileImage(url: String) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(1.dp, MaterialTheme.colorScheme.background, CircleShape)
    ) {
        ImgWithUrl(
            url = url,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun GalleryImage(url: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        ImgWithUrl(
            url = url,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ImgWithUrl(
    url: String,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}
