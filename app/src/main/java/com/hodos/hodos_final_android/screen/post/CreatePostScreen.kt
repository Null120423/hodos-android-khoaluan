package com.hodos.hodos_final_android.screen.post

import android.Manifest
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.FlipCameraAndroid
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hodos.hodos_final_android.LocalNavController
import com.hodos.hodos_final_android.component.Loading
import com.hodos.hodos_final_android.component.NotifyPopup
import com.hodos.hodos_final_android.component.NotifyType
import com.hodos.hodos_final_android.component.UserGalleryView
import com.hodos.hodos_final_android.di.PostViewModelEntryPoint
import com.hodos.hodos_final_android.model.CreatePostRequest
import com.hodos.hodos_final_android.view_model.LocationTagViewModel
import com.hodos.hodos_final_android.view_model.PostCreationStep
import dagger.hilt.android.EntryPointAccessors

@Composable
fun CreatePostScreen(
) {
    val context = LocalContext.current
    val viewModel = remember {
        EntryPointAccessors
            .fromApplication(context, PostViewModelEntryPoint::class.java)
            .postViewModel()
    }
    val currentStep = viewModel.currentStep.value


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        // Step 1: Select Media
        AnimatedVisibility(
            visible = currentStep == PostCreationStep.SELECT_MEDIA,
            enter = fadeIn() + slideInHorizontally { -it },
            exit = fadeOut() + slideOutHorizontally { -it }
        ) {
            SelectMediaScreen(
                selectedImages = viewModel.selectedImages,
                onAddImage = { viewModel.addImage(it) },
                onRemoveImage = { viewModel.removeImage(it) },
                onContinue = {
                    if (viewModel.selectedImages.isNotEmpty()) {
                        viewModel.moveToNextStep()
                    }
                }
            )
        }

        // Step 2: Add Caption
        AnimatedVisibility(
            visible = currentStep == PostCreationStep.ADD_CAPTION,
            enter = fadeIn() + slideInHorizontally { it },
            exit = fadeOut() + slideOutHorizontally { it }
        ) {
            AddCaptionScreen(
                selectedImages = viewModel.selectedImages,
                caption = viewModel.caption.value,
                onCaptionChange = { viewModel.updateCaption(it) },
                onBack = { viewModel.moveToPreviousStep() },
            )
        }
    }
}

@Composable
fun SelectMediaScreen(
    selectedImages: List<Uri>,
    onAddImage: (Uri) -> Unit,
    onRemoveImage: (Uri) -> Unit,
    onContinue: () -> Unit
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Camera state
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var isCapturingPhoto by remember { mutableStateOf(false) }

    // UI state
    var isCapturingMode by remember { mutableStateOf(true) }
    var hasCameraPermission by remember { mutableStateOf(false) }


    // Request camera permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    // Multiple image picker launcher
    val multipleImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        uris.forEach { onAddImage(it) }
        isCapturingMode = false
    }

    // Request camera permission on launch
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    fun bindCameraUseCases() {
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        try {
            cameraProvider?.unbindAll()

            // Create preview
            preview = Preview.Builder().build()

            // Setup image capture
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            // Bind use cases
            cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Log.e("CameraX", "Binding failed", e)
        }
    }

    // Setup camera
    fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            // Setup image capture
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            // Bind camera
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(context))
    }

    // Take photo
    fun takePhoto() {
        val imageCapture = imageCapture ?: return
        isCapturingPhoto = true

        // Create output options
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        // Capture the image
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri
                    savedUri?.let { onAddImage(it) }
                    isCapturingPhoto = false
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
                    isCapturingPhoto = false
                }
            }
        )
    }
    val previewView = remember { PreviewView(context).apply {
        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        scaleType = PreviewView.ScaleType.FILL_CENTER
    }}

    LaunchedEffect(preview) {
        preview?.setSurfaceProvider(previewView.surfaceProvider)
    }

    // Initialize camera when permission is granted
    LaunchedEffect(hasCameraPermission) {
        if (hasCameraPermission) {
            Log.i("API", "SET UP")
            setUpCamera()
        }
    }

    // Clean up camera resources when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().navigationBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Title
            Text(
                text = "New Post",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Continue button
            Text(
                text = "Next",
                color = if (selectedImages.isNotEmpty()) Color(0xFF0095F6) else Color.White.copy(alpha = 0.5f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(enabled = selectedImages.isNotEmpty()) {
                    onContinue()
                }
            )
        }

        // Main content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (isCapturingMode && hasCameraPermission) {
                // Camera preview
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )

                // Capture indicator
                if (isCapturingPhoto) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            } else if (!hasCameraPermission && isCapturingMode) {
                // No camera permission
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Camera permission required",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0095F6)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Grant Permission")
                        }
                    }
                }
            } else {
                UserGalleryView(
                    onImagesSelected = onAddImage,
                    onRemoveImage = onRemoveImage,
                    selectedImages = selectedImages
                )
            }
        }

        // Selected images preview
        AnimatedVisibility(visible = selectedImages.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF121212))
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Selected (${selectedImages.size})",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(80.dp)
                ) {
                    items(selectedImages) { uri ->
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(4.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(context)
                                        .data(uri)
                                        .crossfade(true)
                                        .build()
                                ),
                                contentDescription = "Selected image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Remove button
                            IconButton(
                                onClick = { onRemoveImage(uri) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF121212))
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Camera mode button
            IconButton(
                onClick = {
                    isCapturingMode = true
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isCapturingMode) Color(0xFF0095F6) else Color(0x33FFFFFF),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Camera,
                    contentDescription = "Camera",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Camera controls (only show in camera mode)
            if (isCapturingMode) {
                // Capture button
                IconButton(
                    onClick = { takePhoto() },
                    modifier = Modifier
                        .size(64.dp)
                        .border(2.dp, Color.White, CircleShape)
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }


            }
//            else {
//                // Spacer when in gallery mode
//                Spacer(modifier = Modifier.width(64.dp))
//
//                // Multiple image selection button
//                IconButton(
//                    onClick = { multipleImagePicker.launch("image/*") },
//                    modifier = Modifier
//                        .size(48.dp)
//                        .background(Color(0x33FFFFFF), CircleShape)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = "Add More",
//                        tint = Color.White,
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
//            }

            // Gallery mode button
            IconButton(
                onClick = {
                    isCapturingMode = false
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (!isCapturingMode) Color(0xFF0095F6) else Color(0x33FFFFFF),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Image,
                    contentDescription = "Gallery",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }



    }

   if(isCapturingMode) {
       Box(
           modifier = Modifier
               .fillMaxWidth()
               .padding(vertical = 80.dp, horizontal = 16.dp),
           contentAlignment = Alignment.BottomEnd
       ) {
           // Flip camera button
           IconButton(
               onClick = {
                   lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                       CameraSelector.LENS_FACING_FRONT
                   } else {
                       CameraSelector.LENS_FACING_BACK
                   }
                   bindCameraUseCases()
               },
               modifier = Modifier
                   .size(48.dp)
                   .background(Color(0x33FFFFFF), CircleShape)
           ) {
               Icon(
                   imageVector = Icons.Rounded.FlipCameraAndroid,
                   contentDescription = "Flip Camera",
                   tint = Color.White,
                   modifier = Modifier.size(24.dp)
               )
           }
       }
   }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddCaptionScreen(
    selectedImages: List<Uri>,
    caption: String,
    onCaptionChange: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: LocationTagViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val postViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, PostViewModelEntryPoint::class.java)
            .postViewModel()
    }
    val navController = LocalNavController.current
    val pagerState = rememberPagerState { selectedImages.size }
    val allLocationState by viewModel.allLocationState.collectAsState()
    val postCreateState by postViewModel.postCreateState.collectAsState()
    fun submitPost() {
        val body = CreatePostRequest(
            caption = caption,
            imageUrls = selectedImages.map { it.toString() }
        )
        postViewModel.createPost()
    }

    LaunchedEffect(postCreateState) {
        if(postCreateState.data != null) {
        }else if(postCreateState.error != null) {
        }
    }


    remember(caption) {
        if (viewModel.caption.value != caption) {
            allLocationState.data?.let { viewModel.updateCaption(caption, caption.length, it) }
        }
        true
    }
    Column(
        modifier = Modifier.fillMaxSize().navigationBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Title
            Text(
                text = "Create Post",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Post button
            Text(
                text = "Post",
                color = if(caption.isNotEmpty())  Color(0xFF0095F6) else MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    if(caption.isNotEmpty()) {
                        submitPost()
                    }
                },
            )
        }

        // Image preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFF121212))
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(selectedImages[page])
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "Selected image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Page indicator
            if (selectedImages.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(selectedImages.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(8.dp)
                                .background(
                                    if (pagerState.currentPage == index) Color.White
                                    else Color.White.copy(alpha = 0.5f),
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }

        EnhancedCaptionField(
            viewModel = viewModel,
            placeholder = "Add caption... (Use # to tag locations)",
            modifier = Modifier.fillMaxWidth().weight(1f),
            onValueChange = onCaptionChange
        )


        Button(
            enabled = caption.isNotEmpty() || !postCreateState.isLoading,
            onClick = {
                submitPost()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(100.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            if(
                postCreateState.isLoading
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.background,
                    strokeWidth = 5.dp,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = "Post",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }

    if(postCreateState.isLoading) {
        Loading("SAVING ... ", color = MaterialTheme.colorScheme.background)
    }

    if(postCreateState.error != null ) {
        NotifyPopup(
            message = postCreateState.error?.message ?: "Server has error!",
            type = NotifyType.ERROR,
            onDismiss = {
                postViewModel.resetCreatePost()
            }
        )
    }else if(postCreateState.data?.message != null) {
        NotifyPopup(
            message = postCreateState.data?.message!!,
            type = NotifyType.SUCCESS,
            onDismiss = {
                postViewModel.resetCreatePost()
                postViewModel.resetFlow()
            }
        )
    }

}
