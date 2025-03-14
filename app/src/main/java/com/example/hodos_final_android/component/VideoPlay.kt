package com.example.hodos_final_android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(videoUrl: String) {
    val context = LocalContext.current

    // Khởi tạo ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true // Tự động phát khi sẵn sàng
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release() // Giải phóng tài nguyên khi thoát khỏi màn hình
        }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.secondary)

    ){
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)).scale(1f).background(MaterialTheme.colorScheme.background) ,
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                }
            }
        )
    }
}


@Preview()
@Composable
fun MainScreen() {
    VideoPlayerScreen(videoUrl = "https://firebasestorage.googleapis.com/v0/b/manager-project-3bc13.appspot.com/o/hodos-hack%2FScreenRecording_09-27-2024%2009-38-09_1.mov?alt=media&token=43dd82b2-811b-4eb9-bc28-9ef61b108565")
}
