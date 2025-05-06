package com.example.hodos_final_android.screen.main.planing

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.hodos_final_android.LoadingViewModel
import com.example.hodos_final_android.component.Header
import com.example.hodos_final_android.component.MainLayout


@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ReviewSummaryCreatePlanningScreen(
    viewModel: LoadingViewModel = viewModel()
) {
  MainLayout(
      content =  {
          Header()
      }
  )
}

