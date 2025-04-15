package com.example.hodos_final_android.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.component.Header
import com.example.hodos_final_android.component.ImgWithUrl

@Composable
fun GalleryFullScreen() {
    val navController = LocalNavController.current

    val images = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<List<String>>("images") ?: emptyList()
    Box{
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(images) { imageUrl ->
                ImgWithUrl(url = imageUrl.toString(),
                    contentScale = ContentScale.Crop,
                            modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(0.dp)))

            }
        }
        Header()

    }
}
