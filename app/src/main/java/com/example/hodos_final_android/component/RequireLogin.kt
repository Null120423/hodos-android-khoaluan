package com.example.hodos_final_android.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AuthButtons(
    onRegisterClick: () -> Unit,
    onSignInClick: () -> Unit
) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(10.dp),
           horizontalArrangement = Arrangement.SpaceEvenly
       ) {
           Button(
               onClick = onRegisterClick,
               colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary), // Màu tím
               modifier = Modifier.weight(1f)
           ) {
               Text(text = "Register", color = Color.White)
           }

           Spacer(modifier = Modifier.width(16.dp))

           OutlinedButton(
               onClick = onSignInClick,
               colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary), // Viền tím
               border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
               modifier = Modifier.weight(1f)
           ) {
               Text(text = "Sign in")
           }
       }


}

