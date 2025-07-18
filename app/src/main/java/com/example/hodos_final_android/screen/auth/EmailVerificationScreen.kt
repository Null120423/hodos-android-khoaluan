package com.example.hodos_final_android.screen.auth

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.model.RegisterModel
import com.example.hodos_final_android.model.ResendCodeModel
import com.example.hodos_final_android.model.VerifyModel
import com.example.hodos_final_android.replaceCurrentWithAnimation
import com.example.hodos_final_android.view_model.AuthViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("DefaultLocale")
@Composable
fun EmailVerificationScreen(
    registerModelJson: String,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val registerModel: RegisterModel = Gson().fromJson(
        registerModelJson, object : TypeToken<RegisterModel>() {}.type
    )
    val navController = LocalNavController.current
    val verifyState by viewModel.verifyState.collectAsState()
    val resendCodeState by viewModel.resendCodeState.collectAsState()

    val verificationCode = remember { mutableStateListOf("", "", "", "") }
    val focusRequesters = remember { List(4) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current
    var timeRemaining by remember { mutableIntStateOf(32) }

    val context = LocalContext.current

    // Animation states
    var contentVisible by remember { mutableStateOf(false) }
    var codeInputVisible by remember { mutableStateOf(false) }
    var buttonVisible by remember { mutableStateOf(false) }
    var shouldRequestFocus by remember { mutableStateOf(false) }

    // Timer countdown effect
    LaunchedEffect(key1 = timeRemaining) {
        if (timeRemaining > 0) {
            delay(1000)
            timeRemaining--
        }
    }

    LaunchedEffect(Unit) {
        contentVisible = true
        delay(300)
        codeInputVisible = true
        delay(200)
        buttonVisible = true
        delay(500)
        shouldRequestFocus = true
    }

    // Separate LaunchedEffect for focus request
    LaunchedEffect(shouldRequestFocus, codeInputVisible) {
        if (shouldRequestFocus && codeInputVisible) {
            try {
                delay(100) // Small delay to ensure UI is ready
                focusRequesters[0].requestFocus()
            } catch (e: Exception) {
                // Handle focus request failure gracefully
                println("Focus request failed: ${e.message}")
            }
        }
    }

    // Rest of your LaunchedEffects remain the same...
    LaunchedEffect(verifyState.response) {
        verifyState.response?.let {
            FancyToast.makeText(context, verifyState.response!!.message, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()

            if(registerModel.password.isEmpty()) {
                navController.popBackStack()
            }else {
                navController.replaceCurrentWithAnimation(Screen.Login.route)
            }
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

    val handleResendCode = {
        val resendCodeModel = ResendCodeModel(
            username = registerModel.username,
            email = registerModel.email
        )
        viewModel.resendVerificationCode(resendCodeModel)
    }

    LaunchedEffect(registerModel.password) {
        if(registerModel.password.isEmpty()) {
            handleResendCode()
        }
    }

    // Format time as MM:SS
    val formattedTime = remember(timeRemaining) {
        String.format("%02d:%02d", timeRemaining / 60, timeRemaining % 60)
    }



    val handleVerifyCode = {
        val verifyModel = VerifyModel(
            username = registerModel.username,
            email = registerModel.email,
            verifyCode = verificationCode.joinToString("")
        )
        viewModel.verify(verifyModel)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.95f)
                    )
                )
            )
    ) {
        // Background overlay for better contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        radius = 1000f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Back button (top left)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Main content with animations
            AnimatedVisibility(
                visible = contentVisible,
                enter = slideInVertically(
                    animationSpec = tween(600, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 3 }
                ) + fadeIn(
                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Email verification icon with animation
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                Color.White.copy(alpha = 0.9f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "Email Verification",
                            modifier = Modifier.size(50.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Title with animation
                    AnimatedContent(
                        targetState = "Check Your Email",
                        transitionSpec = {
                            slideInVertically { -it } + fadeIn() togetherWith
                                    slideOutVertically { it } + fadeOut()
                        },
                        label = "title_animation"
                    ) { title ->
                        Text(
                            text = title,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "We've sent a 4-digit verification code to\n${registerModel.email}",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Verification code input with animation
            AnimatedVisibility(
                visible = codeInputVisible,
                enter = slideInVertically(
                    animationSpec = tween(500, delayMillis = 300, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 2 }
                ) + fadeIn(
                    animationSpec = tween(500, delayMillis = 300, easing = FastOutSlowInEasing)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter Verification Code",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // In the verification code input section, update the Row:
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(4) { index ->
                            AnimatedVerificationDigitInput(
                                value = verificationCode[index],
                                onValueChange = { input ->
                                    if (input.length == 1) {
                                        verificationCode[index] = input
                                        if (index < 3) {
                                            try {
                                                focusRequesters[index + 1].requestFocus()
                                            } catch (e: Exception) {
                                                // Handle focus request failure gracefully
                                                println("Focus request failed for index ${index + 1}: ${e.message}")
                                            }
                                        } else {
                                            keyboardController?.hide()
                                        }
                                    } else if (input.isEmpty()) {
                                        verificationCode[index] = ""
                                        if (index > 0) {
                                            try {
                                                focusRequesters[index - 1].requestFocus()
                                            } catch (e: Exception) {
                                                // Handle focus request failure gracefully
                                                println("Focus request failed for index ${index - 1}: ${e.message}")
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.focusRequester(focusRequesters[index]),
                                delay = index * 100,
                                isVisible = codeInputVisible
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Timer and resend section
            AnimatedVisibility(
                visible = codeInputVisible,
                enter = fadeIn(
                    animationSpec = tween(500, delayMillis = 500, easing = FastOutSlowInEasing)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Timer
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Code expires in: ",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        AnimatedContent(
                            targetState = formattedTime,
                            transitionSpec = {
                                slideInVertically { it } + fadeIn() togetherWith
                                        slideOutVertically { -it } + fadeOut()
                            },
                            label = "timer_animation"
                        ) { time ->
                            Text(
                                text = time,
                                fontSize = 14.sp,
                                color = if (timeRemaining <= 10) Color.Red else MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Resend code
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Didn't receive code? ",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        TextButton(
                            onClick = handleResendCode,
                            enabled = timeRemaining == 0 && !resendCodeState.isLoading
                        ) {
                            if (resendCodeState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Resend Code",
                                    color = if (timeRemaining == 0) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Verify button with animation
            AnimatedVisibility(
                visible = buttonVisible,
                enter = slideInVertically(
                    animationSpec = tween(500, delayMillis = 600, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 2 }
                ) + fadeIn(
                    animationSpec = tween(500, delayMillis = 600, easing = FastOutSlowInEasing)
                )
            ) {
                Button(
                    onClick = handleVerifyCode,
                    enabled = verificationCode.all { it.isNotEmpty() } && !verifyState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    if (verifyState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Verify Email",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Help text
            AnimatedVisibility(
                visible = buttonVisible,
                enter = fadeIn(
                    animationSpec = tween(500, delayMillis = 800, easing = FastOutSlowInEasing)
                )
            ) {
                Text(
                    text = "Check your spam folder if you don't see the email",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun AnimatedVerificationDigitInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    delay: Int = 0,
    isVisible: Boolean = true
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            kotlinx.coroutines.delay(delay.toLong())
            visible = true
        } else {
            visible = false
        }
    }

    androidx.compose.animation.AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(400, easing = FastOutSlowInEasing),
            initialScale = 0.3f
        ) + fadeIn(
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.size(60.dp),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                cursorColor = Color.White,
                focusedContainerColor =  MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.Black.copy(alpha = 0.1f)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}
