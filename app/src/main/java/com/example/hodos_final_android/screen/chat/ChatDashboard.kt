package com.example.hodos_final_android.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.component.Title
import com.example.hodos_final_android.component.Txt
import com.example.hodos_final_android.di.ChatViewModelEntryPoint
import com.example.hodos_final_android.model.SuggestQuestion
import com.example.hodos_final_android.navigateWithAnimation
import dagger.hilt.android.EntryPointAccessors


@Composable
fun ChatDashboard(
) {
    val context = LocalContext.current
    val chatViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, ChatViewModelEntryPoint::class.java)
            .chatViewModel()
    }
    val suggestQuestion by chatViewModel.suggestQuestionState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    val navController = LocalNavController.current
    fun onSendMessage(message: String) {
        navController.currentBackStackEntry?.savedStateHandle?.set("message", message)
        navController.navigate(Screen.ChatAiRoom.route)
    }

    LaunchedEffect(suggestQuestion.data) {
        if(suggestQuestion.data == null) {
            chatViewModel.chatDashboard()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        // Header
        ChatbotHeader()

        // Chat content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Bot message
                BotMessage(message = "Question suggestion?")

                Spacer(modifier = Modifier.height(16.dp))

                if(suggestQuestion.isLoading) {
                    Loading()
                }
                // Travel options grid
                suggestQuestion.data?.let { TravelOptionsGrid(options = it) }
            }
        }

        // Input field
        ChatInputField(
            value = inputText,
            onValueChange = { inputText = it },
            onSend = {
                inputText = ""
                onSendMessage(inputText)
            }
        )
    }
}

@Composable
fun ChatbotHeader() {
    val navController = LocalNavController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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

        Spacer(modifier = Modifier.weight(1f))

        // Chat button
        Button(
            onClick = { navController.navigateWithAnimation(Screen.ChatAiRoom.route) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0E0E0),
                contentColor = Color(0xFF2196F3)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Chat",
                tint = Color(0xFF2196F3)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "Xem hội thoại",
                color = Color(0xFF2196F3)
            )
        }
    }
}

@Composable
fun BotMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Title(
            value = message,
            size = 18,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun TravelOptionsGrid(options: List<SuggestQuestion>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(options) { option ->
            TravelOptionCard(option)
        }
    }
}

@Composable
fun TravelOptionCard(options: SuggestQuestion) {
    val navController = LocalNavController.current
    fun onSendMessage(message: String) {
        navController.currentBackStackEntry?.savedStateHandle?.set("message", message)
        navController.navigate(Screen.ChatAiRoom.route)
    }
    Card(
        onClick = {
            onSendMessage(options.message)
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ImgWithUrl(
                url = options.img,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Txt(
                value = options.message,
            )
        }
    }
}

