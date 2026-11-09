
package com.example.gymud.screens.trainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymud.data.model.Chat
import com.example.gymud.screens.common.*
import com.example.gymud.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    userId: String = "",
    otherUserId: String = "",
    otherUserName: String = "Mensajes",
    onBack: () -> Unit = {},
    chatViewModel: ChatViewModel = viewModel()
) {
    val messages by chatViewModel.messages.collectAsState()
    val isSending by chatViewModel.isSending.collectAsState()
    val errorMessage by chatViewModel.errorMessage.collectAsState()

    var messageText by remember { mutableStateOf("") }

    LaunchedEffect(userId, otherUserId) {
        chatViewModel.loadMessages(userId, otherUserId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymudDark)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = GymudYellow
                )
            }

            Column {
                Text(
                    text = otherUserName,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Conversación",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (userId.isBlank() || otherUserId.isBlank()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Selecciona una conversación para comenzar.",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Text(
                            text = "Aún no hay mensajes. ¡Inicia la conversación!",
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                items(messages, key = { it.id }) { message ->
                    MessageBubble(
                        message = message,
                        isMine = message.senderId == userId
                    )
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = {
                        Text(
                            "Escribe un mensaje...",
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isSending,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = GymudSurface,
                        focusedContainerColor = GymudSurface,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        cursorColor = GymudYellow
                    ),
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank() && !isSending) {
                            chatViewModel.sendMessage(messageText)
                            messageText = ""
                        }
                    },
                    enabled = messageText.isNotBlank() && !isSending
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Enviar mensaje",
                        tint = GymudYellow
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: Chat,
    isMine: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Surface(
            color = if (isMine) GymudYellow else GymudSurface,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.message,
                color = if (isMine) Color.Black else Color.White,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
