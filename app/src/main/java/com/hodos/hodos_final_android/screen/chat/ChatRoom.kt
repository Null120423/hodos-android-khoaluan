package com.hodos.hodos_final_android.screen.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hodos.hodos_final_android.LocalNavController
import com.hodos.hodos_final_android.R
import com.hodos.hodos_final_android.Screen
import com.hodos.hodos_final_android.component.AnimateImg
import com.hodos.hodos_final_android.component.AnimatedTypingText
import com.hodos.hodos_final_android.component.ImgWithUrl
import com.hodos.hodos_final_android.di.ChatViewModelEntryPoint
import com.hodos.hodos_final_android.model.ChatWithBotBody
import com.hodos.hodos_final_android.model.Recommendation
import com.hodos.hodos_final_android.navigateWithAnimation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatMessage @RequiresApi(Build.VERSION_CODES.O) constructor(
    val message: String,
    val isFromUser: Boolean,
    val recommendations: List<Recommendation>? = emptyList(),
    val timestamp: String = "",
    val reason: String? = "",
    val description: String? = "",
    val id :  String
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatRoomScreen() {
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
    val chatViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, ChatViewModelEntryPoint::class.java)
            .chatViewModel()
    }
    val chatState by chatViewModel.chatState.collectAsState()
    val focusManager = LocalFocusManager.current
    val navController = LocalNavController.current
    val message = navController.previousBackStackEntry?.savedStateHandle?.get<String>("message") ?: ""
    var isTyping by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Configure status bar
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = Color.White,
            darkIcons = true
        )
    }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                message = message,
                isFromUser = true,
                id =  UUID.randomUUID().toString()
            )
        )
    }

    LaunchedEffect(chatState.isLoading) {
        isTyping = chatState.isLoading
    }

    LaunchedEffect(messages.size) {
        coroutineScope.launch {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(isTyping) {
        coroutineScope.launch {
            listState.animateScrollToItem(messages.size + 1)
        }
    }

    LaunchedEffect(message) {
        if (chatState.data == null && !chatState.isLoading) {
            val chatWithBotBody = ChatWithBotBody(message = message)
            chatViewModel.chatBox(chatWithBotBody)
        }
    }

    LaunchedEffect(chatState.data) {
        chatState.data?.let {
            ChatMessage(
                message = it.message,
                isFromUser = false,
                recommendations = chatState.data?.recommendations,
                reason = it.reason,
                description = it.description,
                id =  UUID.randomUUID().toString()
            )
        }?.let {
            messages.add(it)
        }
    }

    val onSend = {
        if (inputText.isNotEmpty()) {
            val userMessage = inputText
            messages.add(
                ChatMessage(
                    message = userMessage,
                    isFromUser = true,
                    id =  UUID.randomUUID().toString()
                )
            )
            inputText = ""
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
            focusManager.clearFocus()

            val chatWithBotBody = ChatWithBotBody(message = userMessage)
            chatViewModel.chatBox(chatWithBotBody)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(0.4f),
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(0.2f),
                        MaterialTheme.colorScheme.primary.copy(0.1f),
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Modern Header
            ModernChatHeader()

            // Chat messages area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = messages,
                        key = { it.id }
                    ) { message ->
                        ModernChatMessageItem(message = message)
                    }
                    item {
                        if (isTyping) {
                            ModernTypingIndicator()
                        }
                    }
                }
            }

            // Modern Input field
            ModernChatInputField(
                value = inputText,
                onValueChange = { inputText = it },
                onSend = onSend
            )
        }

        // Error handling
        if (chatState.error != null) {
            ModernErrorMessage(error = chatState.error!!.message)
        }
    }
}

@Composable
private fun ModernChatHeader() {
    val navController = LocalNavController.current

    Surface(
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(44.dp)
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // AI Avatar and info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "avatar_pulse")
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.05f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulse_scale"
                    )

                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .size(48.dp)
                            .graphicsLayer(scaleX = pulseScale, scaleY = pulseScale)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primary.copy(0.2f),
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Psychology,
                                contentDescription = "AI Assistant",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "HodosLite AI",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        Color(0xFF10B981),
                                        CircleShape
                                    )
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "Always active",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun ModernChatMessageItem(message: ChatMessage) {
    var showReason by remember { mutableStateOf(true) }
    var showDescription by remember { mutableStateOf(true) }


    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            animationSpec = tween(400, easing = FastOutSlowInEasing),
            initialOffsetY = { it / 3 }
        ) + fadeIn(animationSpec = tween(400))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
        ) {
            if (message.message.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!message.isFromUser) {
                        // AI Avatar for bot messages
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Psychology,
                                contentDescription = "AI",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp),
                                tint =  MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Column(
                        horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp,
                                bottomStart = if (message.isFromUser) 20.dp else 6.dp,
                                bottomEnd = if (message.isFromUser) 6.dp else 20.dp
                            ),
                            color = if (message.isFromUser)  MaterialTheme.colorScheme.primary else  MaterialTheme.colorScheme.secondary,
                            shadowElevation = if (message.isFromUser) 8.dp else 2.dp,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Text(
                                text = message.message,
                                color = if (message.isFromUser) Color.White else Color(0xFF1E293B),
                                fontSize = 15.sp,
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        // Timestamp
                        Text(
                            text = message.timestamp,
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8),
                            modifier = Modifier.padding(
                                top = 4.dp,
                                start = if (message.isFromUser) 0.dp else 8.dp,
                                end = if (message.isFromUser) 8.dp else 0.dp
                            )
                        )
                    }

                    if (message.isFromUser) {
                        Spacer(modifier = Modifier.width(8.dp))
                        // User Avatar
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF10B981).copy(alpha = 0.1f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "User",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp),
                                tint = Color(0xFF10B981)
                            )
                        }
                    }
                }
            }

        }
    }

    if(!message.isFromUser) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6F6)),
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!message.reason.isNullOrBlank()) {
                    AnimatedVisibility(visible = true) {
                        AnimatedTypingText(
                            fullText = "💡 ${message.reason}",
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFF555555)
                            )
                        )
                    }

                }
                if (!message.description.isNullOrBlank()) {
                    AnimatedVisibility(visible = true) {
                        Spacer(modifier = Modifier.height(4.dp))
                        AnimatedTypingText(
                            fullText = message.description,
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.DarkGray
                            )
                        )
                    }

                }
            }
        }


        // Recommendations
        if (!message.recommendations.isNullOrEmpty() && showDescription && showReason) {
            Spacer(modifier = Modifier.height(12.dp))
            ModernRecommendationsSection(recommendations = message.recommendations)
        }
    }

}

@Composable
private fun ModernRecommendationsSection(recommendations: List<Recommendation>) {
    Column {
        Text(
            text = "Recommended Places",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(recommendations) { recommendation ->
                ModernRecommendationCard(recommendation = recommendation)
            }
        }
    }
}

@Composable
private fun ModernRecommendationCard(recommendation: Recommendation) {
    val navController = LocalNavController.current
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "recommendation_scale"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier
            .width(180.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                navController.navigateWithAnimation(
                    Screen.LocationDetailScreen.createRoute(recommendation.id)
                )
            }
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Image
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Box {
                    ImgWithUrl(
                        url = recommendation.img,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )

                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            recommendation.name?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1E293B),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            recommendation.address?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            recommendation.reason?.let {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = it,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
}

@Composable
private fun ModernTypingIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // AI Avatar
        Surface(
            shape = CircleShape,
            color = Color(0xFF667eea).copy(alpha = 0.1f),
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Psychology,
                contentDescription = "AI",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                tint = Color(0xFF667eea)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Typing animation
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            AnimateImg(
                source = R.raw.typing,
                modifier = Modifier.height(40.dp)
            )
        }
    }
}

@Composable
private fun ModernChatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        color = Color.White,
        shadowElevation = 12.dp,
        modifier = Modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Input field
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFF8FAFC),
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = {
                        Text(
                            text = "Type your message...",
                            color = Color(0xFF94A3B8),
                            fontSize = 16.sp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color(0xFF1E293B),
                        unfocusedTextColor = Color(0xFF1E293B)
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            onSend()
                            keyboardController?.hide()
                        }
                    ),
                    maxLines = 4
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Send button
            Surface(
                shape = CircleShape,
                color = if (value.isNotBlank()) Color(0xFF667eea) else Color(0xFFE2E8F0),
                shadowElevation = if (value.isNotBlank()) 8.dp else 0.dp,
                modifier = Modifier.size(48.dp)
            ) {
                IconButton(
                    onClick = onSend,
                    enabled = value.isNotBlank(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (value.isNotBlank()) Color.White else Color(0xFF94A3B8),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernErrorMessage(error: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFEF4444).copy(alpha = 0.1f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Error,
                contentDescription = "Error",
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = error,
                color = Color(0xFFEF4444),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


