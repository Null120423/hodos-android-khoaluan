package com.example.hodos_final_android.screen.main.chat


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: Int,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: String,
    val isRead: Boolean = false,
    val senderName: String = ""
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatRoomScreen() {
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = 1,
                message = "Hello Nice",
                isFromUser = false,
                timestamp = "Livechat 02:10 PM"
            ),
            ChatMessage(
                id = 2,
                message = "Welcome to LiveChat\nI was made with . Pick a topic from the list or type down a question!",
                isFromUser = false,
                timestamp = ""
            ),
            ChatMessage(
                id = 3,
                message = "Welcome",
                isFromUser = true,
                timestamp = "Visitor 02:12 PM",
                isRead = true
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        ChatHeader()

        // Chat agent info
        ChatAgentInfo()

        // Divider
        Divider(color = Color.LightGray, thickness = 1.dp)

        // Chat messages
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
                    if (message.timestamp.isNotEmpty() && message.id > 1) {
                        Text(
                            text = message.timestamp,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            textAlign = if (message.isFromUser) TextAlign.End else TextAlign.Start
                        )
                    } else if (message.id == 1) {
                        Text(
                            text = message.timestamp,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Start
                        )
                    }

                    ChatMessageItem(message = message)
                }
            }
        }

        // Input field
        ChatInputField(
            value = inputText,
            onValueChange = { inputText = it },
            onSend = {
                if (inputText.isNotEmpty()) {
                    messages.add(
                        ChatMessage(
                            id = messages.size + 1,
                            message = inputText,
                            isFromUser = true,
                            timestamp = "Visitor ${getCurrentTime()}",
                            isRead = false
                        )
                    )
                    inputText = ""
                    coroutineScope.launch {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                }
            }
        )
    }
}

@Composable
fun ChatHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bot avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF2196F3))
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Bot Avatar",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Bot name and status
        Column {
            Text(
                text = "TRIPBot",
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
fun ChatAgentInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Agent avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF3F51B5))
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Agent Avatar",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Agent info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Chatbot",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = "Support Agent",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Thumbs up/down
        IconButton(onClick = { /* Handle thumbs up */ }) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = "Thumbs Up",
                tint = Color.Gray
            )
        }

        IconButton(onClick = { /* Handle thumbs down */ }) {
            Icon(
                imageVector = Icons.Default.ThumbDown,
                contentDescription = "Thumbs Down",
                tint = Color.Gray
            )
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

        if (message.isFromUser && message.isRead) {
            Text(
                text = "Read",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, end = 4.dp)
            )
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
            .background(Color(0xFFF5F5F5))
            .padding(8.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text("Câu hỏi của bạn là gì chính xác?")
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
