package com.example.hodos_final_android.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.component.AnimateImg
import com.example.hodos_final_android.component.BtnOutline
import com.example.hodos_final_android.component.MainLayout

@Composable
fun ComingSoonScreen(
) {


    MainLayout(
        content = {
            AnimateImg(source = R.raw.comming_soon)

        }

    )


}

