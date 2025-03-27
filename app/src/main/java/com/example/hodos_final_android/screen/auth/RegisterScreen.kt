package com.example.hodos_final_android.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.ColumnCenter
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.PasswordInput
import com.example.hodos_final_android.component.RowCenter
import com.example.hodos_final_android.component.RowStart
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.TextBtn
import com.example.hodos_final_android.component.TextInput
import com.example.hodos_final_android.component.Title
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.navigateWithAnimation

@Preview()
@Composable
fun RegisterScreen() {
    val navController = LocalNavController.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }

   MainLayout(
       content = {
           ColumnCenter(
               modifier = Modifier
                   .fillMaxSize()
                   .verticalScroll(rememberScrollState())
           ) {
               RowCenter{
                   Image(
                       painter = painterResource(id = R.drawable.hodos),
                       contentDescription = "App Logo",
                       modifier = Modifier.size(50.dp),
                       contentScale = ContentScale.Fit
                   )
               }

               Seprate(height = 24)

               Title(
                   value = "Let's Get Started",
                   size = 24,
                   fontWeight = FontWeight.Bold
               )

               Seprate(height = 8)

               Txt(
                   value = "create your new account and find more\nbeautiful destinations",
                   color = Color.Gray,
                   textAlign = TextAlign.Center
               )

               Seprate(height = 32)

               TextInput(
                   label = "Name",
                   value = name,
                   onChange = { name = it },
                   placeholder = "Enter your full name",
               )

               Seprate(height = 16)

               TextInput(
                   label = "Email",
                   value = email,
                   onChange = { email = it },
                   placeholder = "Enter your email",
               )

               Seprate(height = 16)

               PasswordInput(
                   label = "Password",
                   password = password,
                   onPasswordChange = {
                       password = it
                   }
               )

               Seprate(height = 16)

               PasswordInput(
                   label = "Re-type Password",
                   password = confirmPassword,
                   onPasswordChange = {
                       confirmPassword = it
                   }
               )

               Seprate(height = 16)

               // Terms of service
               RowStart(
                   modifier = Modifier.fillMaxWidth()
               ) {
                   Checkbox(
                       checked = acceptTerms,
                       onCheckedChange = { acceptTerms = it },
                       colors = CheckboxDefaults.colors(
                           checkedColor = MaterialTheme.colorScheme.primary
                       ),
                       modifier = Modifier.height(10.dp)
                   )

                   TextBtn(
                       onClick = {},
                       title = "Accept term of service",
                       color = Color(0xFFFF5252),
                       size = 14
                   )
               }

               Seprate(height = 24)

               // Sign Up Button
               BtnPrimary(
                   title = "Sign Up",
                   onClick = {
                       navController.navigateWithAnimation(Screen.EmailVerification.route)
                   },
                   modifier = Modifier
                       .fillMaxWidth()
                       .clip(RoundedCornerShape(8.dp))
               )

               Seprate(height = 24)

               // Login Text
               RowCenter(
                   modifier = Modifier.padding(bottom = 24.dp),
               ) {
                   Txt(
                       value = "Already have an account? ",
                       size = 14,
                       color = Color.Gray
                   )

                   TextBtn(
                       title = "Sign In",
                       color = Color(0xFFFF5252),
                       size = 14,
                       fontWeight = FontWeight.Medium
                   )
               }

               Seprate(height = 8)
           }
           // App Logo

       }
   )
}