package com.example.hodos_final_android.screen.main.chat


import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.R
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.AnimateImg
import com.example.hodos_final_android.component.IconBtn
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.component.RowBetween
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.di.ChatViewModelEntryPoint
import com.example.hodos_final_android.model.ChatWithBotBody
import com.example.hodos_final_android.model.Recommendation
import com.example.hodos_final_android.navigateWithAnimation
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

data class ChatMessage(
    val message: String,
    val isFromUser: Boolean,
    val recommendations: List<Recommendation>? = emptyList()
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatRoomScreen(
) {
    val context = LocalContext.current
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

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                message = message,
                isFromUser = true,
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
        val chatWithBotBody = ChatWithBotBody(message = message)
        chatViewModel.chatBox(chatWithBotBody)
    }

    LaunchedEffect(chatState.data) {
        chatState.data?.let {
            ChatMessage(
                message = it.message,
                isFromUser = false,
                recommendations = chatState.data?.recommendations
            )
        }?.let {
            messages.add(
                it
            )
        }
    }

    val onSend = {
        if (inputText.isNotEmpty()) {
            val userMessage = inputText
            messages.add(
                ChatMessage(
                    message = userMessage,
                    isFromUser = true,
                )
            )
            inputText = ""
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
            focusManager.clearFocus()

            // Gửi lên chatbot
            val chatWithBotBody = ChatWithBotBody(message = userMessage)
            chatViewModel.chatBox(chatWithBotBody)

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        // Sticky Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .zIndex(1f)
        ) {
            ChatHeader()
            Divider(color = Color.LightGray, thickness = 1.dp)
        }

        // Chat messages scrollable area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { message ->
                    ChatMessageItem(message = message)
                }
                item {
                    if (isTyping) {
                        ShimmeringChatBubble()
                    }
                }
            }
        }

        // Input field
        ChatInputField(
            value = inputText,
            onValueChange = { inputText = it },
            onSend = onSend
        )
    }

}

@SuppressLint("RememberReturnType")
@Composable
fun ShimmeringChatBubble() {
    AnimateImg(
        source = R.raw.typing,
        modifier = Modifier.height(100.dp)
    )
}


@Composable
fun ChatHeader() {
    val navController = LocalNavController.current
    RowBetween(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        IconBtn(
            imgVector = Icons.Default.ArrowBackIosNew,
            onClick = {
                navController.popBackStack()
            }
        )
        Column {
            Text(
                text = "HodosLite",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Always active",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
    ) {
        if(message.message != "") {
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (message.isFromUser) 16.dp else 4.dp,
                            bottomEnd = if (message.isFromUser) 4.dp else 16.dp
                        )
                    )
                    .background(
                        if (message.isFromUser) Color(0xFF2196F3) else Color(0xFFF5F5F5)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = message.message,
                    color = if (message.isFromUser) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }
        }
        if (!message.recommendations.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                message.recommendations.forEach { recommendation ->
                    RecommendationCard(recommendation = recommendation)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
@Composable
fun RecommendationCard(recommendation: Recommendation) {
    Log.i("API", recommendation.reason)
    val navController = LocalNavController.current
    Card(
        onClick = {
            navController.navigateWithAnimation(Screen.LocationDetailScreen.createRoute(recommendation.id))
        },
        modifier = Modifier
            .width(180.dp)
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ImgWithUrl(url = recommendation.images[0], modifier = Modifier.height(50.dp))
            Txt(value = recommendation.name, fontWeight = FontWeight.Bold)
            Txt(value = recommendation.reason)
        }
    }
}


@Composable
fun ChatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text("Please enter your question?")
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(onClick = onSend) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Send",
                        tint = Color(0xFF2196F3)
                    )
                }
            },
            maxLines = 1,
            singleLine = true
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentTime(): String {
    val hour = java.time.LocalTime.now().hour
    val minute = java.time.LocalTime.now().minute
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    return String.format("%02d:%02d %s", hour12, minute, amPm)
}
