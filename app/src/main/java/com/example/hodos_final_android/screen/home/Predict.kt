package com.example.hodos_final_android.screen.home


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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.BtnOutline
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.ColumnCenter
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.NoResultDialog
import com.example.hodos_final_android.component.PredictDialog
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.helper.getScreenHeight
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.helper.toSdp
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.service.AIModelHelper
import kotlinx.coroutines.delay

@Composable
fun PredictScreen(
    navController: NavController
) {
    val isShowBottomSheet = remember { mutableStateOf(false) }
    val isShowDialogNotFoundLocation = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val aiHelper = remember { AIModelHelper(context) }
    val isSearching = remember { mutableStateOf(false) }

    // Bitmap state
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }


    // Classify image logic
    LaunchedEffect(bitmapState.value) {
        bitmapState.value?.let {
            isSearching.value = true
            isShowBottomSheet.value = false
            val label = aiHelper.classifyImage(it)

            delay(1000)
            if (label != null) {
                navController.navigateWithAnimation(Screen.PredictResultScreen.createRoute(label) )
            } else {
                Log.e("PredictScreen", "Classification data is null")
                Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                isShowDialogNotFoundLocation.value = true
            }

            isSearching.value = false
        }
    }

    // Camera launcher
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
            Toast.makeText(context, "Quyền Camera bị từ chối", Toast.LENGTH_SHORT).show()
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

    val prefs = context.getSharedPreferences("gallery_permission", Context.MODE_PRIVATE)
    val hasRequestedPermissionBefore = prefs.getBoolean("has_requested", false)

    val activity = context as Activity

// 1. Chọn đúng permission theo Android version
    val imagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

// 2. Gallery launcher
    val singleGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            bitmapState.value = bitmap
        }
    }

// 3. Request permission launcher
    val requestPermissionLauncherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            singleGalleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Quyền truy cập ảnh đã bị từ chối", Toast.LENGTH_SHORT).show()
        }
    }

// 4. Hàm gọi mở gallery có xử lý popup quyền
    val handleSelectSinglePhotoFromGallery = {
        when {
            ContextCompat.checkSelfPermission(context, imagePermission) == PackageManager.PERMISSION_GRANTED -> {
                // Quyền đã cấp
                singleGalleryLauncher.launch("image/*")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(activity, imagePermission) && !hasRequestedPermissionBefore -> {
                // Hiển thị giải thích nếu chưa từng hiện
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
                // Yêu cầu quyền lần đầu
                prefs.edit().putBoolean("has_requested", true).apply()
                requestPermissionLauncherGallery.launch(imagePermission)
            }

            else -> {
                // Đã từng từ chối và không hiển thị lại
                Toast.makeText(
                    context,
                    "Permission denied. You can enable it in app settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    MainLayout(
        backgroundImg = R.drawable.predict_bg,
        isBgBlur = true,
        isVisibleBottomSheet = isShowBottomSheet,
        bottomSheetContent = {
           Box(modifier = Modifier.height(200.dp)){
                    ColumnCenter(
                        modifier = Modifier.padding(20.dp)
                    ){
                        Text(
                            "Choose an option",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        BtnPrimary(
                            rounded = 100,
                            title = "Choose from Gallery",
                            minWidth = getScreenWidth(),
                            onClick = handleSelectSinglePhotoFromGallery
                        )

                        Seprate(height = 10)

                        BtnOutline(
                            rounded = 100,
                            title = "Take photo",
                            minWidth = getScreenWidth(),
                            onClick = handleTakePhoto
                        )

                    }
                }
        },
        content = {
            ColumnCenter(
                modifier = Modifier.height(getScreenHeight().toSdp()),
            ) {
                AnalysisFeature(
                    onPredict = {
                        isShowBottomSheet.value = true
                    }
                )
            }

            if(isSearching.value ) {
                PredictDialog(
                    onDismiss ={
                        isSearching.value = false
                    }
                )
            }

            if(isShowDialogNotFoundLocation.value) {
                NoResultDialog(
                    title = "Location not found by Image!",
                    onDismiss = {
                        isShowDialogNotFoundLocation.value = false
                    }
                )
            }

        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    bottomSheetContent: @Composable (ColumnScope.() -> Unit)? = null,
    onDismiss : () ->  Unit

) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState
    ) {
        if (bottomSheetContent != null) {
            bottomSheetContent()
        }
    }
}

