package com.example.hodos_final_android.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hodos_final_android.helper.getScreenWidth

@Composable
fun AuthButtons(
    onRegisterClick: () -> Unit,
    onSignInClick: () -> Unit
) {
       RowBetween(
           modifier = Modifier
               .fillMaxWidth()
       ) {
           BtnPrimary(
               title = "Register",
               onClick = onRegisterClick,
               rounded = 100,
               minWidth = getScreenWidth() / 2 - 20
           )
           Seprate(height = 10, width = 16)
           BtnOutline(
               title = "Sign in",
               onClick = onSignInClick,
               minWidth = getScreenWidth() / 2 - 20,
               rounded = 100,
               )
       }

}

