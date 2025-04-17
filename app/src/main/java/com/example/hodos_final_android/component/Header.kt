package com.example.hodos_final_android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.LocalNavController

@Composable()
fun Header(
    title: String? = ""
) {
    val navController = LocalNavController.current

    RowBetween(modifier = Modifier.padding(top = 50.dp, bottom = 10.dp, end = 20.dp , start = 20.dp).background(
        Color.Transparent
    )) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .size(40.dp)
                .background(Color(0x88000000), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        if (title != null) {
            Title(value = title, fontWeight = FontWeight.Bold)
        }

        Box(modifier = Modifier.width(40.dp))
    }
}