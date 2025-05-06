package com.example.hodos_final_android.screen.post

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.component.MainLayout

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PostDetailScreen() {
    val navController = LocalNavController.current

    val url = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("url") ?: ""

    var isLoading by remember { mutableStateOf(true) }

    MainLayout(
        modifier = Modifier.padding(0.dp),
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.javaScriptEnabled = true
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                }
                            }
                            loadUrl(url)
                        }
                    }
                )

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Loading()
                    }
                }

            }
        }
    )
}
