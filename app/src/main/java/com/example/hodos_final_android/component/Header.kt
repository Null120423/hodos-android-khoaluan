package com.example.hodos_final_android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.LocalNavController

@Composable()
fun Header(

) {
    val navController = LocalNavController.current

    RowBetween(modifier = Modifier.padding(top = 20.dp, bottom = 10.dp, end = 20.dp , start = 20.dp).background(
        Color.Transparent
    )) {
        IconBtn(
            imgVector = Icons.Filled.ArrowBackIosNew,
            onClick = {
                navController.popBackStack()
            }
        )
    }
}