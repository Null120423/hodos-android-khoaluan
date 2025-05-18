package com.example.hodos_final_android.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Loading(
    title: String? = "Processing loading ...",
    color : Color = MaterialTheme.colorScheme.tertiary
) {
   Box(
       modifier = Modifier.fillMaxWidth().fillMaxHeight()
       .pointerInput(Unit) {},
   ) {
       Column(
           modifier = Modifier
               .padding(40.dp)
               .fillMaxSize()
               .wrapContentSize(Alignment.Center)
           ,
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
       ) {
           CircularProgressIndicator(
               color = MaterialTheme.colorScheme.primary,
               strokeWidth = 5.dp,
               modifier = Modifier.size(50.dp)
           )
           Seprate(height = 20)
           if (title != null) {
               Title(
                   fontWeight = FontWeight.Bold,
                   value = title,
                   color = color
               )
           }
       }
   }
}
