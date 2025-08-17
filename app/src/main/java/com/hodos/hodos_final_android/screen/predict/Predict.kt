package com.hodos.hodos_final_android.screen.predict

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.hodos.hodos_final_android.Screen
import com.hodos.hodos_final_android.component.NoResultDialog
import com.hodos.hodos_final_android.component.PredictDialog
import com.hodos.hodos_final_android.navigateWithAnimation
import com.hodos.hodos_final_android.service.AIModelHelper
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PredictScreen(
    navController: NavController
) {
    val isShowBottomSheet = remember { mutableStateOf(false) }
    val isShowDialogNotFoundLocation = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val aiHelper = remember { AIModelHelper(context) }
    val isSearching = remember { mutableStateOf(false) }
    val systemUiController = rememberSystemUiController()

    // Bitmap state
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }

    // Configure status bar
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = Color(0xFF1A1A1A),
            darkIcons = false
        )
    }

    // Classify image logic (keeping original logic)
    LaunchedEffect(bitmapState.value) {
        bitmapState.value?.let {
            isSearching.value = true
            isShowBottomSheet.value = false
            val label = aiHelper.classifyImg(it)

            delay(1000)
            if (label != null) {
                navController.navigateWithAnimation(Screen.PredictResultScreen.createRoute(label))
            } else {
                Log.e("PredictScreen", "Classification data is null")
                Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                isShowDialogNotFoundLocation.value = true
            }

            isSearching.value = false
        }
    }

    // Camera launcher (keeping original logic)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.extras?.get("data")?.let { photo ->
            bitmapState.value = photo as Bitmap
        }
    }

    val requestPermissionLauncherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val handleTakePhoto = {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent)
        } else {
            requestPermissionLauncherCamera.launch(Manifest.permission.CAMERA)
        }
    }

    // Gallery logic (keeping original logic)
    val prefs = context.getSharedPreferences("gallery_permission", Context.MODE_PRIVATE)
    val hasRequestedPermissionBefore = prefs.getBoolean("has_requested", false)
    val activity = context as Activity

    val imagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val singleGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            bitmapState.value = bitmap
        }
    }

    val requestPermissionLauncherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            singleGalleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Gallery permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val handleSelectSinglePhotoFromGallery = {
        when {
            ContextCompat.checkSelfPermission(context, imagePermission) == PackageManager.PERMISSION_GRANTED -> {
                singleGalleryLauncher.launch("image/*")
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, imagePermission) && !hasRequestedPermissionBefore -> {
                AlertDialog.Builder(context)
                    .setTitle("Permission Required")
                    .setMessage("This app needs access to your gallery to select images.")
                    .setPositiveButton("Allow") { _, _ ->
                        prefs.edit().putBoolean("has_requested", true).apply()
                        requestPermissionLauncherGallery.launch(imagePermission)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
            !hasRequestedPermissionBefore -> {
                prefs.edit().putBoolean("has_requested", true).apply()
                requestPermissionLauncherGallery.launch(imagePermission)
            }
            else -> {
                Toast.makeText(
                    context,
                    "Permission denied. You can enable it in app settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Background with gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF667eea).copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    )
                )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header
           stickyHeader {
               ModernHeader(
                   onBackClick = { navController.popBackStack() }
               )
           }

           item {
               Spacer(modifier = Modifier.height(40.dp))

               // Main Content
               ModernPredictContent(
                   onPredictClick = { isShowBottomSheet.value = true }
               )

               Spacer(modifier = Modifier.height(40.dp))

               // Features Section
               ModernFeaturesSection()

               Spacer(modifier = Modifier.height(120.dp))
           }
        }

        // Bottom Sheet
        if (isShowBottomSheet.value) {
            ModernBottomSheet(
                onDismiss = { isShowBottomSheet.value = false },
                onCameraClick = {
                    isShowBottomSheet.value = false
                    handleTakePhoto()
                },
                onGalleryClick = {
                    isShowBottomSheet.value = false
                    handleSelectSinglePhotoFromGallery()
                }
            )
        }

        // Dialogs (keeping original logic)
        if (isSearching.value) {
            PredictDialog(
                onDismiss = {
                    isSearching.value = false
                }
            )
        }

        if (isShowDialogNotFoundLocation.value) {
            NoResultDialog(
                title = "Location not found by Image!",
                onDismiss = {
                    isShowDialogNotFoundLocation.value = false
                }
            )
        }
    }
}

@Composable
private fun ModernHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .statusBarsPadding()
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        Surface(
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.1f),
            modifier = Modifier.size(44.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Title
        Text(
            text = "AI Predict",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Help button
        Surface(
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.1f),
            modifier = Modifier.size(44.dp)
        ) {
            IconButton(
                onClick = { /* Handle help */ },
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    Icons.Default.Help,
                    contentDescription = "Help",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ModernPredictContent(
    onPredictClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "predict_scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // Main prediction card
        Surface(
            shape = RoundedCornerShape(26.dp),
            shadowElevation = 20.dp,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(scaleX = scale, scaleY = scale)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(0.8f),
                                MaterialTheme.colorScheme.primary.copy(0.9f),
                                MaterialTheme.colorScheme.primary.copy(0.6f),
                                MaterialTheme.colorScheme.primary.copy(0.8f),
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isPressed = true
                        onPredictClick()
                    }
            ) {
                // Background pattern
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.1f),
                                    Color.Transparent
                                ),
                                radius = 400f
                            )
                        )
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    // AI Icon with animation
                    val infiniteTransition = rememberInfiniteTransition(label = "ai_pulse")
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulse_scale"
                    )

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .graphicsLayer(scaleX = pulseScale, scaleY = pulseScale)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "AI Predict",
                            modifier = Modifier.size(50.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "AI Image Recognition",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Upload or capture an image to discover amazing locations with our advanced AI technology",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Predict button
                    Surface(
                        shape = RoundedCornerShape(28.dp),
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(horizontal = 32.dp, vertical = 16.dp)
                        ) {
                            Icon(
                                Icons.Default.PhotoCamera,
                                contentDescription = "Start",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF667eea)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Start Prediction",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF667eea)
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
}

@Composable
private fun ModernFeaturesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "How it works",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        val features = listOf(
            FeatureItem(
                icon = Icons.Default.PhotoLibrary,
                title = "Upload Image",
                description = "Choose from gallery or take a new photo",
                color = Color(0xFF10B981)
            ),
            FeatureItem(
                icon = Icons.Default.Psychology,
                title = "AI Analysis",
                description = "Our AI analyzes your image instantly",
                color = Color(0xFF8B5CF6)
            ),
            FeatureItem(
                icon = Icons.Default.LocationOn,
                title = "Get Results",
                description = "Discover location details and recommendations",
                color = Color(0xFFFF6B35)
            )
        )

        features.forEachIndexed { index, feature ->
            ModernFeatureCard(
                feature = feature,
                delay = index * 200
            )
            if (index < features.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class FeatureItem(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val color: Color
)

@Composable
private fun ModernFeatureCard(
    feature: FeatureItem,
    delay: Int = 0
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            animationSpec = tween(500, easing = FastOutSlowInEasing),
            initialOffsetX = { it }
        ) + fadeIn(animationSpec = tween(500))
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 8.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            feature.color.copy(alpha = 0.2f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        feature.icon,
                        contentDescription = feature.title,
                        modifier = Modifier.size(24.dp),
                        tint = feature.color
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = feature.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = feature.description,
                        fontSize = 14.sp,
                        color = Color.Gray.copy(alpha = 0.7f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernBottomSheet(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .height(4.dp)
                    .width(40.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
                        RoundedCornerShape(2.dp)
                    )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Choose Image Source",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Gallery option
            ModernBottomSheetOption(
                icon = Icons.Default.PhotoLibrary,
                title = "Choose from Gallery",
                description = "Select an existing photo",
                color = Color(0xFF667eea),
                onClick = onGalleryClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Camera option
            ModernBottomSheetOption(
                icon = Icons.Default.PhotoCamera,
                title = "Take Photo",
                description = "Capture a new image",
                color = Color(0xFFFF6B35),
                onClick = onCameraClick
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ModernBottomSheetOption(
    icon: ImageVector,
    title: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "option_scale"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color.copy(alpha = 0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    modifier = Modifier.size(28.dp),
                    tint = color
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray.copy(alpha = 0.7f)
                )
            }

            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Go",
                modifier = Modifier.size(20.dp),
                tint = Color.Gray.copy(alpha = 0.5f)
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
}
