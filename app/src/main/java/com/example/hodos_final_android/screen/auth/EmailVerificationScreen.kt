package com.example.hodos_final_android.screen.auth


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.BtnPrimary
import com.example.hodos_final_android.component.MainLayout
import com.example.hodos_final_android.component.RowCenter
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.component.TextBtn
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.model.RegisterModel
import com.example.hodos_final_android.model.ResendCodeModel
import com.example.hodos_final_android.model.VerifyModel
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.view_model.AuthViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Composable
fun EmailVerificationScreen( registerModelJson: String, viewModel: AuthViewModel = hiltViewModel()) {
    val registerModel: RegisterModel = Gson().fromJson(
        registerModelJson, object : TypeToken<RegisterModel>() {}.type
    )
    val navController = LocalNavController.current
    val verifyState by viewModel.verifyState.collectAsState()
    val resendCodeState by viewModel.resendCodeState.collectAsState()

    val verificationCode = remember { mutableStateListOf("", "", "", "") }
    val focusRequesters = List(4) { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var timeRemaining by remember { mutableIntStateOf(32) }

    val context = LocalContext.current
    // Timer countdown effect
    LaunchedEffect(key1 = timeRemaining) {
        if (timeRemaining > 0) {
            delay(1000)
            timeRemaining--
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    LaunchedEffect(verifyState.response) {
        verifyState.response?.let {
            FancyToast.makeText(context, verifyState.response!!.message, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()

            navController.navigateWithAnimation(Screen.Login.route)
        }
    }

    LaunchedEffect(resendCodeState.response) {
        resendCodeState.response?.let {
            FancyToast.makeText(context, resendCodeState.response!!.message, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()
            timeRemaining = 32
        }
    }

    LaunchedEffect(verifyState.error) {
        if (verifyState.error != null) {
            FancyToast.makeText(context, verifyState.error!!.message, FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show()
        }
    }

    LaunchedEffect(resendCodeState.error) {
        if (resendCodeState.error != null) {
            FancyToast.makeText(context, resendCodeState.error!!.message, FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show()
        }
    }

    // Format time as MM:SS
    val formattedTime = remember(timeRemaining) {
        String.format("%02d:%02d", timeRemaining / 60, timeRemaining % 60)
    }

    val handleResendCode = {
        val resendCodeModel = ResendCodeModel(
            username = registerModel.username,
            email = registerModel.email
        )
        viewModel.resendVerificationCode(resendCodeModel)
    }

    val handleVerifyCode = {
        val verifyModel = VerifyModel(
            username = registerModel.username,
            email = registerModel.email,
            verifyCode =verificationCode.joinToString("")
        )

        viewModel.verify(verifyModel)
    }


    MainLayout(
        isLoading = verifyState.isLoading or resendCodeState.isLoading,
        content = {
            Seprate(height = 60)

            // Verification Icon
            Image(
                painter = painterResource(id = R.drawable.hodos),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Seprate(height = 24)

            Txt(
                value = "Check Your Email",
                size = 24,
                fontWeight = FontWeight.Bold
            )

            Seprate(height = 8)

            Txt(
                value = "We've sent the code to the email on\nyour device",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Seprate(height = 32)

            // Verification Code Input

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(4) { index ->
                    VerificationDigitInput(
                        value = verificationCode[index],
                        onValueChange = { input ->
                            if (input.length == 1) {
                                verificationCode[index] = input
                                if (index < 3) {
                                    focusRequesters[index + 1].requestFocus()
                                } else {
                                    keyboardController?.hide() // Ẩn bàn phím khi nhập xong
                                }
                            } else if (input.isEmpty()) {
                                verificationCode[index] = ""
                                if (index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            }
                        },
                        modifier = Modifier.focusRequester(focusRequesters[index])
                    )
                }
            }


            Seprate(height = 24)

            // Timer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Txt(
                    value = "Code expires in: ",
                    size = 14,
                    color = Color.Gray
                )

                Txt(
                    value = formattedTime,
                    size = 14,
                    color = Color(0xFFFF5252),
                    fontWeight = FontWeight.Medium
                )
            }

            Seprate(height = 16)

            // Resend Code
            RowCenter {
                Txt(
                    value = "Didn't receive code? ",
                    size = 14,
                    color = Color.Gray
                )

                TextBtn(
                    onClick = handleResendCode,
                    title = "Resend Code",
                    color = Color(0xFFFF5252),
                    size = 14,
                    fontWeight = FontWeight.Medium
                )
            }

            Seprate(height = 48)

            // Verify Button
            BtnPrimary(
                title = "Verify",
                onClick = handleVerifyCode,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    )
}
@Composable
fun VerificationDigitInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .size(50.dp),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
