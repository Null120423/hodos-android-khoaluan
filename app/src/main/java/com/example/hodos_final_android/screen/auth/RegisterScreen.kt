package com.example.hodos_final_android.screen.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.model.RegisterModel
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.replaceCurrentWithAnimation
import com.example.hodos_final_android.view_model.AuthViewModel
import com.shashank.sony.fancytoastlib.FancyToast

@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val signUpState by viewModel.signUpState.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }
    var showSocialRegister by remember { mutableStateOf(false) }

    LaunchedEffect(signUpState.response) {
        signUpState.response?.let {
            FancyToast.makeText(context, signUpState.response!!.message, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()
            val registerModel = RegisterModel(
                username = username,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )
            navController.replaceCurrentWithAnimation(Screen.EmailVerification.createRoute(registerModel))
            viewModel.clear()
        }
    }

    LaunchedEffect(signUpState.error) {
        if (signUpState.error != null) {
            FancyToast.makeText(context, signUpState.error!!.message, FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
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
                            Color.White.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
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

            Spacer(modifier = Modifier.height(20.dp))

            // Animated Content
            AnimatedContent(
                targetState = showSocialRegister,
                transitionSpec = {
                    if (targetState) {
                        // Switching to social register
                        slideInVertically(
                            animationSpec = tween(500, easing = FastOutSlowInEasing),
                            initialOffsetY = { it }
                        ) + fadeIn(
                            animationSpec = tween(500, easing = FastOutSlowInEasing)
                        ) togetherWith slideOutVertically(
                            animationSpec = tween(300, easing = FastOutSlowInEasing),
                            targetOffsetY = { -it / 2 }
                        ) + fadeOut(
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )
                    } else {
                        // Switching to traditional register
                        slideInVertically(
                            animationSpec = tween(500, easing = FastOutSlowInEasing),
                            initialOffsetY = { -it }
                        ) + fadeIn(
                            animationSpec = tween(500, easing = FastOutSlowInEasing)
                        ) togetherWith slideOutVertically(
                            animationSpec = tween(300, easing = FastOutSlowInEasing),
                            targetOffsetY = { it / 2 }
                        ) + fadeOut(
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )
                    }
                },
                label = "register_mode_transition"
            ) { isSocial ->
                if (isSocial) {
                    SocialRegisterContent(
                        onEmailRegisterClick = { showSocialRegister = false },
                        onLoginClick = {
                            navController.navigateWithAnimation(Screen.Login.route)
                        }
                    )
                } else {
                    TraditionalRegisterContent(
                        username = username,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        acceptTerms = acceptTerms,
                        isLoading = signUpState.isLoading,
                        onUsernameChange = { username = it },
                        onEmailChange = { email = it },
                        onPasswordChange = { password = it },
                        onConfirmPasswordChange = { confirmPassword = it },
                        onAcceptTermsChange = { acceptTerms = it },
                        onRegisterClick = {
                            viewModel.signUp(email, password, username, confirmPassword)
                        },
                        onSocialRegisterClick = { showSocialRegister = true },
                        onLoginClick = {
                            navController.navigateWithAnimation(Screen.Login.route)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SocialRegisterContent(
    onEmailRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var buttonsVisible by remember { mutableStateOf(false) }
    var dividerVisible by remember { mutableStateOf(false) }
    var iconVisible by remember { mutableStateOf(false) }
    var loginVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        buttonsVisible = true
        kotlinx.coroutines.delay(200)
        dividerVisible = true
        kotlinx.coroutines.delay(200)
        iconVisible = true
        kotlinx.coroutines.delay(200)
        loginVisible = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo with animation
        AnimatedVisibility(
            visible = true,
            enter = scaleIn(
                animationSpec = tween(600, easing = FastOutSlowInEasing),
                initialScale = 0.3f
            ) + fadeIn(
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Color.White.copy(alpha = 0.9f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hodos),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title with animation
        AnimatedContent(
            targetState = "Create Free Account",
            transitionSpec = {
                slideInVertically { -it } + fadeIn() togetherWith
                        slideOutVertically { it } + fadeOut()
            },
            label = "title_animation"
        ) { title ->
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Join us and discover amazing features\nwith your new account",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Social Register Buttons with staggered animation
        androidx.compose.animation.AnimatedVisibility(
            visible = buttonsVisible,
            enter = slideInHorizontally(
                animationSpec = tween(500, easing = FastOutSlowInEasing),
                initialOffsetX = { it }
            ) + fadeIn(
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
        ) {
            Column {
                SocialRegisterButton(
                    icon = R.drawable.face_logo,
                    text = "Continue with Facebook",
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    onClick = { /* Handle Facebook register */ }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SocialRegisterButton(
                    icon = R.drawable.google_color_svgrepo_com,
                    text = "Continue with Google",
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    onClick = { /* Handle Google register */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Divider with animation
        androidx.compose.animation.AnimatedVisibility(
            visible = dividerVisible,
            enter = fadeIn(
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            ) + expandHorizontally(
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.White.copy(alpha = 0.3f)
                )
                Text(
                    text = "OR",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.White.copy(alpha = 0.3f)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Email icon with animation
        androidx.compose.animation.AnimatedVisibility(
            visible = iconVisible,
            enter = scaleIn(
                animationSpec = tween(400, easing = FastOutSlowInEasing),
                initialScale = 0.3f
            ) + fadeIn(
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            )
        ) {
            CircularIconButton(
                icon = R.drawable.person,
                onClick = onEmailRegisterClick
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        // Login text with animation
        androidx.compose.animation.AnimatedVisibility(
            visible = loginVisible,
            enter = slideInVertically(
                animationSpec = tween(500, easing = FastOutSlowInEasing),
                initialOffsetY = { it / 2 }
            ) + fadeIn(
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
        ) {
            Row(
                modifier = Modifier.padding(bottom = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = onLoginClick
                ) {
                    Text(
                        text = "Sign In",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun TraditionalRegisterContent(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    acceptTerms: Boolean,
    isLoading: Boolean,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onAcceptTermsChange: (Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    onSocialRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var formVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        formVisible = true
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    Color.White.copy(alpha = 0.9f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.hodos),
                contentDescription = "App Logo",
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Welcome text with animation
        AnimatedContent(
            targetState = "Let's Get Started",
            transitionSpec = {
                slideInVertically { it } + fadeIn() togetherWith
                        slideOutVertically { -it } + fadeOut()
            },
            label = "welcome_animation"
        ) { title ->
            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create your new account and find more\nbeautiful destinations",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Form fields with staggered animation
        androidx.compose.animation.AnimatedVisibility(
            visible = formVisible,
            enter = slideInVertically(
                animationSpec = tween(400, easing = FastOutSlowInEasing),
                initialOffsetY = { it / 4 }
            ) + fadeIn(
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            )
        ) {
            Column {
                ModernTextInput(
                    label = "Full Name",
                    value = username,
                    onChange = onUsernameChange,
                    placeholder = "Enter your full name"
                )

                Spacer(modifier = Modifier.height(16.dp))

                ModernTextInput(
                    label = "Email",
                    value = email,
                    onChange = onEmailChange,
                    placeholder = "Enter your email"
                )

                Spacer(modifier = Modifier.height(16.dp))

                ModernPasswordInput(
                    label = "Password",
                    password = password,
                    onPasswordChange = onPasswordChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                ModernPasswordInput(
                    label = "Confirm Password",
                    password = confirmPassword,
                    onPasswordChange = onConfirmPasswordChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Terms checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = acceptTerms,
                        onCheckedChange = onAcceptTermsChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = Color.White.copy(alpha = 0.6f)
                        )
                    )
                    TextButton(
                        onClick = { /* Handle terms */ }
                    ) {
                        Text(
                            text = "Accept terms of service",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Register Button
                Button(
                    onClick = onRegisterClick,
                    enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty() && acceptTerms,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Sign Up",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Back to social register
                TextButton(
                    onClick = onSocialRegisterClick
                ) {
                    Text(
                        text = "← Back to social register",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Login text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    TextButton(
                        onClick = onLoginClick
                    ) {
                        Text(
                            text = "Sign In",
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Seprate(
                    height = 40
                )
            }
        }
    }
}

@Composable
private fun SocialRegisterButton(
    icon: Int,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(28.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}

@Composable
private fun CircularIconButton(
    icon: Int,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(64.dp)
            .background(
                Color.White.copy(alpha = 0.95f),
                CircleShape
            )
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun ModernTextInput(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.White.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                cursorColor = Color.White,
                focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.Black.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun ModernPasswordInput(
    label: String,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = {
                Text(
                    text = "Enter your password",
                    color = Color.White.copy(alpha = 0.5f)
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                cursorColor = Color.White,
                focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.Black.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}
