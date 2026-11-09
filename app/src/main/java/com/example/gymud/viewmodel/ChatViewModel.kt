
package com.example.gymud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymud.data.model.Chat
import com.example.gymud.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _messages = MutableStateFlow<List<Chat>>(emptyList())
    val messages: StateFlow<List<Chat>> = _messages.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var currentUserId = ""
    private var otherUserId = ""

    // Load messages from a conversation
    fun loadMessages(userId: String, receiverId: String) {
        if (userId.isBlank() || receiverId.isBlank()) {
            _errorMessage.value = "Invalid conversation"
            return
        }

        if (currentUserId == userId && otherUserId == receiverId) {
            return
        }

        currentUserId = userId
        otherUserId = receiverId

        viewModelScope.launch {
            repository.getMessagesStream(userId, receiverId).collect { messageList ->
                _messages.value = messageList
            }
        }
    }

    // Send a message to the selected user
    fun sendMessage(message: String) {
        if (message.isBlank()) return

        if (currentUserId.isBlank() || otherUserId.isBlank()) {
            _errorMessage.value = "Conversation not available"
            return
        }

        viewModelScope.launch {
            _isSending.value = true
            _errorMessage.value = null

            val result = repository.sendMessage(
                userId = currentUserId,
                otherUserId = otherUserId,
                message = message
            )

            result.onFailure { error ->
                _errorMessage.value = error.message ?: "Could not send message"
            }

            _isSending.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
