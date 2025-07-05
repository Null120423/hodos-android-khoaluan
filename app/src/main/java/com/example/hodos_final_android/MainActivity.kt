package com.example.hodos_final_android

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hodos_final_android.helper.NetworkStateMonitor
import com.example.hodos_final_android.helper.OnboardingUtils
import com.example.hodos_final_android.screen.OnboardingScreen
import com.example.hodos_final_android.theme.HodosTheme
import com.example.hodos_final_android.view_model.LoginWithFacebookDto
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.security.MessageDigest
import kotlin.io.encoding.ExperimentalEncodingApi

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("NavController not provided")
}

val LocalLogInWithSocial = staticCompositionLocalOf<((LoginWithFacebookDto) -> Unit) -> Unit> {
    error("HandleLoginFacebook not provided")
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var networkMonitor: NetworkStateMonitor
    private val onboardingUtils by lazy { OnboardingUtils(this) }
    private lateinit var callbackManager: CallbackManager

    @OptIn(ExperimentalEncodingApi::class)
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkMonitor = NetworkStateMonitor(this)
        networkMonitor.register()
        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()

        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            for (signature in info.signingInfo?.apkContentsSigners!!) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val keyHash = Base64.encodeToString(md.digest(),  Base64.DEFAULT)
                Log.d("KeyHash", "KeyHash: $keyHash")
            }
        } catch (e: Exception) {
            Log.e("KeyHash", "Error getting key hash", e)
        }



        fun onLoginFacebook(onLogin : (body: LoginWithFacebookDto) -> Unit) {
            LoginManager.getInstance().logInWithReadPermissions(
                this@MainActivity,
                listOf("email", "public_profile")
            )

            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        val accessToken = result.accessToken
                        Log.d("FB_LOGIN", "AccessToken: ${accessToken.token}")

                        val request = GraphRequest.newMeRequest(accessToken) { obj, _ ->
                            try {
                                val id = obj?.getString("id")
                                val name = obj?.getString("name")
                                val email = obj?.optString("email")

                                // Avatar URL theo chuẩn Facebook Graph
                                val avatar = "https://graph.facebook.com/$id/picture?type=large"

                                Log.d("FB_PROFILE", "ID: $id")
                                Log.d("FB_PROFILE", "Name: $name")
                                Log.d("FB_PROFILE", "Email: $email")
                                Log.d("FB_PROFILE", "Avatar: $avatar")

                                if(name != null && email != null && avatar != null) {
                                    val dataLogin = LoginWithFacebookDto(
                                        fullname = name,
                                        email = email,
                                        avatar = avatar
                                    )
                                    onLogin(dataLogin)
                                }

                            } catch (e: Exception) {
                                Log.e("FB_PROFILE", "Parsing error", e)
                            }
                        }

                        // Yêu cầu các trường thông tin cần lấy
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,email")
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        Log.d("FB_LOGIN", "Login canceled")
                    }

                    override fun onError(error: FacebookException) {
                        Log.e("FB_LOGIN", "Login error", error)
                    }
                })
        }

        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        val logInWithSocial: ((LoginWithFacebookDto) -> Unit) -> Unit = { callback ->
            onLoginFacebook(onLogin = callback)
        }



        setContent {
            val navController = rememberNavController()
            val isNetworkAvailable by networkMonitor.isConnected.collectAsState()

            HodosTheme {
                CompositionLocalProvider(LocalNavController provides navController, LocalLogInWithSocial provides logInWithSocial) {
                    Box(modifier = Modifier.fillMaxSize()){
                        if (onboardingUtils.isOnboardingCompleted()) {
                            AppNavHost(navController)
                        } else {
                            ShowOnboardingScreen()
                        }

                        Box(modifier = Modifier.fillMaxSize()) {
                            // Nội dung chính app (onboarding hoặc AppNavHost)
                            if (onboardingUtils.isOnboardingCompleted()) {
                                AppNavHost(navController)
                            } else {
                                ShowOnboardingScreen()
                            }
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(bottom = 0.dp)
//                                    .align(Alignment.BottomCenter) // Chỉ dùng được nếu trong BoxScope
//                            ) {
//                                AnimatedVisibility(
//                                    visible = !isNetworkAvailable,
//                                    enter = slideInVertically(
//                                        initialOffsetY = { it } // Từ dưới lên
//                                    ) + fadeIn(),
//                                    exit = slideOutVertically(
//                                        targetOffsetY = { it } // Trượt xuống
//                                    ) + fadeOut()
//                                ) {
//                                    Box(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .background(MaterialTheme.colorScheme.primary)
//                                    ) {
//                                        Row(modifier = Modifier.padding(10.dp)) {
//                                            Txt(
//                                                value = "Network is not connected!",
//                                                fontWeight = FontWeight.Bold,
//                                                color = MaterialTheme.colorScheme.background
//                                            )
//                                        }
//                                    }
//                                }
//                            }
                        }


                    }
                }

            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun ShowOnboardingScreen() {
        val navController = LocalNavController.current
        val scope = rememberCoroutineScope()
        var showOnboarding by remember { mutableStateOf(true) }

        AnimatedContent(
            targetState = showOnboarding,
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { it }) with
                        slideOutHorizontally(targetOffsetX = { -it }) using
                        SizeTransform(clip = false)
            }
        ) { isOnboarding ->
            if (isOnboarding) {
                OnboardingScreen {
                    onboardingUtils.setOnboardingCompleted()
                    scope.launch {
                        showOnboarding = false
                    }
                }
            } else {
                AppNavHost(navController)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        networkMonitor.unregister()
    }
}
