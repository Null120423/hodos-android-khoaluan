package com.example.hodos_final_android.component

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserGalleryView(
    onImagesSelected: (Uri) -> Unit ,
    onRemoveImage: (Uri) -> Unit,
    selectedImages: List<Uri>
) {
    val context = LocalContext.current

    // State for selected images
    val selectedImages = remember { mutableStateListOf<Uri>().apply { addAll(selectedImages) } }

    // State for all images from MediaStore
    var mediaStoreImages by remember { mutableStateOf<List<MediaStoreImage>>(emptyList()) }

    // Loading state
    var isLoading by remember { mutableStateOf(true) }

    // Permission state
    var hasStoragePermission by remember { mutableStateOf(false) }

    // Check if we have permission
    val hasPermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasStoragePermission = isGranted
    }

    // Request permission if needed
    LaunchedEffect(Unit) {
        if (hasPermission) {
            hasStoragePermission = true
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    // Load images from MediaStore
    LaunchedEffect(hasStoragePermission) {
        if (hasStoragePermission) {
            isLoading = true
            mediaStoreImages = loadImagesFromMediaStore(context)
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when {
            !hasStoragePermission -> {
                // Permission denied view
                PermissionDeniedView(
                    onRequestPermission = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                )
            }
            isLoading -> {
                // Loading view
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF0095F6)
                    )
                }
            }
            mediaStoreImages.isEmpty() -> {
                // No images found view
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No images found",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                // Images grid
                Column(modifier = Modifier.fillMaxSize()) {
                    // Selected count
                    if (selectedImages.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF121212))
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${selectedImages.size} selected",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }

                    // Image grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(mediaStoreImages) { image ->
                            val isSelected = selectedImages.contains(image.uri)

                            MediaStoreImageItem(
                                image = image,
                                isSelected = isSelected,
                                onImageClick = {
                                    if (isSelected) {
                                        selectedImages.remove(image.uri)
                                        onRemoveImage(image.uri)
                                    } else {
                                        selectedImages.add(image.uri)
                                        onImagesSelected(image.uri)
                                    }
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun MediaStoreImageItem(
    image: MediaStoreImage,
    isSelected: Boolean,
    onImageClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color(0xFF0095F6) else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onImageClick)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context)
                    .data(image.uri)
                    .crossfade(true)
                    .build()
            ),
            contentDescription = "Gallery image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Selection indicator
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .background(Color(0xFF0095F6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun PermissionDeniedView(
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Storage Permission Required",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "We need access to your photos to display them here.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRequestPermission,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0095F6)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Grant Permission",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

data class MediaStoreImage(
    val id: Long,
    val uri: Uri,
    val name: String,
    val dateAdded: Long
)

suspend fun loadImagesFromMediaStore(context: android.content.Context): List<MediaStoreImage> {
    return withContext(Dispatchers.IO) {
        val images = mutableListOf<MediaStoreImage>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                images.add(MediaStoreImage(id, contentUri, name, dateAdded))
            }
        }

        images
    }
}
