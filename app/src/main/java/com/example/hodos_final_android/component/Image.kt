package com.example.hodos_final_android.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.hodos_final_android.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImgWithUrl(
    url: String,
    alt: String = url,
    size: Int = 30,
    isAvatar: Boolean = false,
    modifier: Modifier = Modifier,
    rounded: Int = 8,
    contentScale : ContentScale = ContentScale.Crop,
) {
    val roundedCus: Int = if (isAvatar) 10000 else rounded
    GlideImage(
        model = url,
        contentDescription = alt,
        modifier = modifier
            .fillMaxWidth()
            .height(size.dp)
            .width(size.dp)
            .clip(RoundedCornerShape(roundedCus.dp)),
        contentScale = contentScale,
    ) {
        it.placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
    }
}

@Composable()
fun ImgSource(
    source: Int,
    alt: String = "",
    modifier: Modifier =  Modifier,
    size: Int = 100
){
    Image(
        painter = painterResource(id = source ),
        contentDescription = alt,
        modifier = modifier.size(size.dp),
        contentScale = ContentScale.Fit
    )
}