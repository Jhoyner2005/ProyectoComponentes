package com.example.gymud.data.model

import com.google.firebase.Timestamp

data class Chat(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now()
)