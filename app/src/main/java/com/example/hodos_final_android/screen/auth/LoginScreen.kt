package com.example.hodos_final_android.screen.auth
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.IconBtn
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.PasswordInput
import com.example.hodos_final_android.component.RowBetween
import com.example.hodos_final_android.component.RowCenter
import com.example.hodos_final_android.component.RowStart
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.TextBtn
import com.example.hodos_final_android.component.TextInput
import com.example.hodos_final_android.component.Title
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.model.LoginModel
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.view_model.AuthViewModel
import com.shashank.sony.fancytoastlib.FancyToast


@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val navController = LocalNavController.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()

    val handleLogin= {
        val login = LoginModel(
            username = username,
            password = password
        )

        viewModel.login(login)
    }


    LaunchedEffect(loginState.data) {
        loginState.data?.let {
            FancyToast.makeText(context, "Login successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()
            navController.popBackStack()
        }
    }

    LaunchedEffect(loginState.error) {
        loginState.error?.let {
            FancyToast.makeText(context, loginState.error!!.message, FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show()
        }
    }


    MainLayout(
        isLoading = loginState.isLoading,
        content = {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.hodos),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

        }

        Seprate(height = 24)

        Title(
            value = "Welcome Back !",
            size = 24,
            fontWeight = FontWeight.Bold
        )

        Seprate(height = 8)

        Txt(
            value = "Stay signed in with your account to make\nsearching easier",
            textAlign = TextAlign.Center
        )


        Seprate(height = 32)

        TextInput(
            label = "Username or Email",
            value = username,
            onChange = { username = it },
            placeholder = "Enter your username",
        )

        Seprate(height = 16)

        PasswordInput(
            label = "Password",
            password = password,
            onPasswordChange = {
                password = it
            }
        )

        Seprate(height = 12)

        // Remember me and Forgot password
        RowBetween {
            RowStart {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.height(10.dp)
                )
                Txt(
                    value = "Keep me signed in",
                    size = 14,
                )
            }

            TextBtn(
                onClick = {},
                title = "Forgot password?",
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        Seprate(height = 12)

        BtnPrimary(
            title = "Login",
            onClick = handleLogin,
            minWidth = getScreenWidth()
        )

        Seprate(height = 24)

        Txt(
            value = "Or continue with",
            color = Color.Gray
        )

        Seprate(height = 16)

        // Social Login Buttons
        RowCenter  {
            IconBtn(
                icon = R.drawable.google_color_svgrepo_com,
                contentDescription = "Login with Facebook"
            )

            Seprate(height = 24, width = 24)

            IconBtn(
                icon = R.drawable.email_icon,
                contentDescription = "Login with Google"
            )

            Seprate(height = 24, width = 24)

            IconBtn(
                icon = R.drawable.face_logo,
                contentDescription = "Login with Twitter"
            )
        }

        Seprate(height = 24)

        // Sign Up Text
        RowCenter(
            modifier = Modifier.padding(bottom = 24.dp),
        ) {
            Txt(
                value = "You don't Have an account? ",
                size = 14,
            )

            TextBtn(
                onClick = {
                    navController.navigateWithAnimation(Screen.Register.route)
                },
                title = "Sign Up",
                color = MaterialTheme.colorScheme.primary,
                size = 14,
                fontWeight = FontWeight.Medium
            )

        }

        Seprate(height = 8)
    })
}

