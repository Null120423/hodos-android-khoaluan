package com.example.hodos_final_android.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.R

@Composable
fun Loading(
    title: String? = "Processing loading ..."
) {
   Column(
       modifier = Modifier
           .padding(40.dp)
           .fillMaxSize()
           .wrapContentSize(Alignment.Center),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center
   ) {
       AnimateImg(
           source = R.raw.typing,
           modifier = Modifier.height(100.dp)
       )
       Seprate(height = 20)
       if (title != null) {
           Title(
               fontWeight = FontWeight.Bold,
               value = title
           )
       }
   }
}
