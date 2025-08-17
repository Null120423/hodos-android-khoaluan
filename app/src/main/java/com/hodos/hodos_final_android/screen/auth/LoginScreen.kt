package com.hodos.hodos_final_android.screen.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.hodos.hodos_final_android.LocalLogInWithSocial
import com.hodos.hodos_final_android.LocalNavController
import com.hodos.hodos_final_android.R
import com.hodos.hodos_final_android.Screen
import com.hodos.hodos_final_android.component.Loading
import com.hodos.hodos_final_android.model.LoginModel
import com.hodos.hodos_final_android.navigateWithAnimation
import com.hodos.hodos_final_android.view_model.AuthViewModel
import com.hodos.hodos_final_android.view_model.LoginWithGoogleDto
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showTraditionalLogin by remember { mutableStateOf(false) }

    // handle FCM token.
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result
            val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString("fcm_token", token).apply()

            Log.e("FCM",  token)

        } else {
            Log.e("FCM", "Fetching FCM token failed", task.exception)
        }
    }

    val loginState by viewModel.loginState.collectAsState()

    val handleLogin = {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val fcmToken = prefs.getString("fcm_token", null)

        val login = LoginModel(
            username = username,
            password = password,
            fcmToken = fcmToken
        )
        viewModel.login(login)
        isLoading = true
    }

    // login gooogle
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        isLoading = true
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        val fcmToken = prefs.getString("fcm_token", null)
                        user?.let {
                            val name = it.displayName
                            val email = it.email
                            val avatar = it.photoUrl?.toString()
                            if(name != null && email != null && avatar != null) {
                                val loginWithGoogleBody = LoginWithGoogleDto(
                                    fullname = name,
                                    email = email,
                                    avatar = avatar,
                                    fcmToken = fcmToken
                                )
                                viewModel.loginWithGoogle(loginWithGoogleBody)

                            }
                        }
                    } else {
                        Log.e("Auth", "Login failed", task.exception)
                    }
                }
        } catch (e: Exception) {
            Log.e("Auth", "Error", e)
        }
    }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("992321399382-604u1kjbh4dpnu749mculrp0561ct01e.apps.googleusercontent.com")
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)


    fun onLoginGoogle() {
        googleSignInClient.signOut().addOnCompleteListener {
            launcher.launch(googleSignInClient.signInIntent)
        }
    }
    // login facebook
    val loginWithFacebook = LocalLogInWithSocial.current

    LaunchedEffect(loginState.data) {
        loginState.data?.let {
            Toast.makeText(context, "Login successfully!", Toast.LENGTH_SHORT).show()
            isLoading = false
            delay(500)
            if (navController.previousBackStackEntry != null) {
                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(loginState.error) {
        loginState.error?.let {
            FancyToast.makeText(context, loginState.error!!.message, FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show()
            isLoading = false
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
        // Background blur effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        ),
                        radius = 800f
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

            // Close button (top right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
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
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Animated Content using AnimatedContent
            AnimatedContent(
                targetState = showTraditionalLogin,
                transitionSpec = {
                    if (targetState) {
                        // Switching to traditional login
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
                        // Switching to social login
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
                label = "login_mode_transition"
            ) { isTraditional ->
                if (isTraditional) {
                    TraditionalLoginContent(
                        username = username,
                        password = password,
                        rememberMe = rememberMe,
                        isLoading = loginState.isLoading,
                        onUsernameChange = { username = it },
                        onPasswordChange = { password = it },
                        onRememberMeChange = { rememberMe = it },
                        onLoginClick = handleLogin,
                        onBackClick = { showTraditionalLogin = false }
                    )
                } else {
                    SocialLoginContent(
                        onLoginFacebook = {
                            loginWithFacebook { facebookDto ->
                                isLoading = true
                                val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                val fcmToken = prefs.getString("fcm_token", null)
                                viewModel.loginWithFacebook(facebookDto, fcmToken)
                            }
                        },
                        onLoginGoogle = {
                            onLoginGoogle()
                        },
                        onEmailLoginClick = { showTraditionalLogin = true },
                        onRegisterClick = {
                            navController.navigateWithAnimation(Screen.Register.route)
                        }
                    )
                }
            }


        }

    }
    if(isLoading) {
        Loading()
    }
}

@Composable
private fun SocialLoginContent(
    onEmailLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginGoogle: () -> Unit,
    onLoginFacebook : () -> Unit
) {
    var buttonsVisible by remember { mutableStateOf(false) }
    var dividerVisible by remember { mutableStateOf(false) }
    var iconVisible by remember { mutableStateOf(false) }
    var signUpVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        buttonsVisible = true
        kotlinx.coroutines.delay(200)
        dividerVisible = true
        kotlinx.coroutines.delay(200)
        iconVisible = true
        kotlinx.coroutines.delay(200)
        signUpVisible = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title with animation
        AnimatedContent(
            targetState = "Sign In",
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
            text = "",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Social Login Buttons with staggered animation
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
                SocialLoginButton(
                    icon = R.drawable.face_logo,
                    text = "Continue with Facebook",
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    onClick = { onLoginFacebook() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SocialLoginButton(
                    icon = R.drawable.google_color_svgrepo_com,
                    text = "Continue with Google",
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    onClick = { onLoginGoogle() }
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
                onClick = onEmailLoginClick
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        // Sign up text with animation
        androidx.compose.animation.AnimatedVisibility(
            visible = signUpVisible,
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
                    text = "You don't have account? ",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = onRegisterClick
                ) {
                    Text(
                        text = "Sign Up",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun TraditionalLoginContent(
    username: String,
    password: String,
    rememberMe: Boolean,
    isLoading: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var formVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        formVisible = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Welcome text with animation
        AnimatedContent(
            targetState = "Welcome Back!",
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
            text = "Stay signed in with your account to make\nsearching easier",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

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
                    label = "Username or Email",
                    value = username,
                    onChange = onUsernameChange,
                    placeholder = "Enter your username"
                )

                Spacer(modifier = Modifier.height(16.dp))

                ModernPasswordInput(
                    label = "Password",
                    password = password,
                    onPasswordChange = onPasswordChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Remember me and forgot password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = onRememberMeChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = Color.White.copy(alpha = 0.6f)
                            )
                        )
                        Text(
                            text = "Keep me signed in",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }

                    TextButton(onClick = { /* Handle forgot password */ }) {
                        Text(
                            text = "Forgot password?",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
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
                            text = "Login",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Back button
                TextButton(
                    onClick = onBackClick
                ) {
                    Text(
                        text = "← Back to social login",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SocialLoginButton(
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
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(28.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
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
                Color.White.copy(alpha = 0.9f),
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
