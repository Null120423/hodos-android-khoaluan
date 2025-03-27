package com.example.hodos_final_android.screen.home


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.hodos_final_android.R
import com.example.hodos_final_android.component.BtnOutline
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.ColumnCenter
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.helper.getScreenHeight
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.helper.toSdp
import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.service.AIModelHelper

@Composable
fun PredictScreen() {
    val showBottomSheet = remember { mutableStateOf(false) }
    var location: MutableState<Location?> = remember { mutableStateOf(null) }
    val context = LocalContext.current
    val aiHelper = remember { AIModelHelper(context) }

    // Bitmap state
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }


    // Classify image logic
    LaunchedEffect(bitmapState.value) {
        bitmapState.value?.let {
            location.value = aiHelper.classifyImage(it)!!
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
            Toast.makeText(context, "Quyền truy cập thư viện bị từ chối", Toast.LENGTH_SHORT).show()
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

    val handleSelectSinglePhotoFromGallery = {
        if(ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) {
            singleGalleryLauncher.launch("image/*")
        }else {
            requestPermissionLauncherGallery.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    MainLayout(
        backgroundImg = R.drawable.predict_bg,
        isBgBlur = true,
        isVisibleBottomSheet = showBottomSheet,
        onCloseBottomSheet = {
            showBottomSheet.value = false
        },
        bottomSheetContent = {
            if(location.value == null) {
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
            }
            else {
                Box(modifier = Modifier.height(getScreenHeight().toSdp())){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        location.value?.let { Text(text = it.name, fontSize = 20.sp, textAlign = TextAlign.Center) }
                        Spacer(modifier = Modifier.height(8.dp))
                        location.value?.let { Text(text = it.address) }
                      //  Text(text = "Confidence: ${(confidence * 100).toInt()}%")
                        Spacer(modifier = Modifier.height(8.dp))
                        location.value?.let {
                            ImgWithUrl(
                                url = it.img,
                                modifier = Modifier.fillMaxWidth().height(200.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        BtnPrimary(
                            title = "Close",
                            onClick = {
                                location.value = null
                                showBottomSheet.value = false
                            }
                        )
                    }
                }
            }

        },
        content = {
            ColumnCenter(
                modifier = Modifier.height(getScreenHeight().toSdp()),
            ) {
                AnalysisFeature(
                    onPredict = {
                        showBottomSheet.value = true
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

