package com.hodos.hodos_final_android.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun MainLayout(
    content: @Composable ColumnScope.() -> Unit, // Bắt buộc
    modifier: Modifier = Modifier.padding(20.dp),
    backgroundImg: Int? = null,
    isBgBlur: Boolean = false,
    isLoading: Boolean = false,
    isVisibleBottomSheet: MutableState<Boolean>? = null,
    onCloseBottomSheet: () -> Unit = {},
    bottomSheetContent: @Composable (ColumnScope.() -> Unit)? = null,
            title: String? = null,
    header: Boolean? = true
) {
    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        backgroundImg?.let { imgRes ->
            Image(
                painter = painterResource(id = imgRes),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        if (isBgBlur) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim)
            )
        }



        // Main Content
        ColumnCenter(modifier = modifier) {
            content()
        }

        // Optional Bottom Sheet
//        if (isVisibleBottomSheet?.value == true) {
//            BottomSheetContent(
//                onDismiss = onCloseBottomSheet,
//                bottomSheetContent = bottomSheetContent
//            )
//        }

        // Optional Loading Dialog
        LoadingDialog(isLoading = isLoading)

        if(header == true) {
            Header(title = title)
        }
    }
}

