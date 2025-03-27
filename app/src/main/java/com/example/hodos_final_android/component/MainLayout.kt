package com.example.hodos_final_android.component

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
import com.example.hodos_final_android.screen.home.BottomSheetContent


@Composable
fun MainLayout(
    content: @Composable ColumnScope.() -> Unit,
    backgroundImg: Int? = null,
    isVisibleBottomSheet: MutableState<Boolean>? = null,
    onCloseBottomSheet: ()-> Unit = {},
    isBgBlur: Boolean? = false,
    bottomSheetContent: @Composable (ColumnScope.() -> Unit)? = null,
    modifier: Modifier = Modifier.padding(20.dp)
) {
    Box {
        backgroundImg?.let { imgRes ->
            Image(
                painter = painterResource(id = imgRes),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        if(isBgBlur == true) {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim)
            )
        }
        Header()
        ColumnCenter(
            modifier = modifier
        ) {
            Seprate(height = 60)
            content()
        }

        if (isVisibleBottomSheet != null) {
            if (isVisibleBottomSheet.value) {
                BottomSheetContent(
                    onDismiss = onCloseBottomSheet,
                    bottomSheetContent = bottomSheetContent
                )
            }
        }
    }
}
