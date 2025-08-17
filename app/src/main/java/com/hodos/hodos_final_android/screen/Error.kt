package com.hodos.hodos_final_android.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hodos.hodos_final_android.LocalNavController
import com.hodos.hodos_final_android.R
import com.hodos.hodos_final_android.component.BtnPrimary
import com.hodos.hodos_final_android.component.ColumnCenter
import com.hodos.hodos_final_android.component.ImgSource
import com.hodos.hodos_final_android.component.Seprate
import com.hodos.hodos_final_android.component.Title
import com.hodos.hodos_final_android.helper.getScreenWidth
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ErrorScreen(
    title: String? = ""
) {
    val navController = LocalNavController.current

    Box(
        modifier = Modifier.padding()
    ) {
        ColumnCenter(
            modifier = Modifier.fillMaxSize()
        ) {

            ImgSource(
                source = R.drawable.error,
                modifier = Modifier.width((getScreenWidth()).dp),
            )

            if (title != null) {
                Seprate(height = 10)
                Title(
                    value = title
                )
            }
            Seprate(height = 10)

            BtnPrimary(
                minWidth = getScreenWidth() / 2,
                title = "BACK",
                onClick = {
                    navController.popBackStack()
                }
            )

        }
    }

}