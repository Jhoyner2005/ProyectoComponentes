
package com.example.gymud.data.repository

import com.example.gymud.data.model.Chat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Generate a unique conversation ID for both participants
    private fun getConversationId(userId: String, otherUserId: String): String {
        return listOf(userId, otherUserId).sorted().joinToString("_")
    }

    // Listen to conversation messages in real time
    fun getMessagesStream(
        userId: String,
        otherUserId: String
    ): Flow<List<Chat>> = callbackFlow {

        if (userId.isBlank() || otherUserId.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val conversationId = getConversationId(userId, otherUserId)

        val listener = firestore.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Chat::class.java)?.copy(
                        id = document.id
                    )
                }?.sortedBy { it.timestamp } ?: emptyList()

                trySend(messages)
            }

        awaitClose {
            listener.remove()
        }
    }

    // Send a new message
    suspend fun sendMessage(
        userId: String,
        otherUserId: String,
        message: String
    ): Result<Unit> {

        return try {
            if (userId.isBlank() || otherUserId.isBlank() || message.isBlank()) {
                return Result.failure(
                    IllegalArgumentException("Invalid message information")
                )
            }

            val conversationId = getConversationId(userId, otherUserId)

            val document = firestore.collection("conversations")
                .document(conversationId)
                .collection("messages")
                .document()

            val newMessage = Chat(
                id = document.id,
                senderId = userId,
                receiverId = otherUserId,
                message = message.trim(),
                timestamp = Timestamp.now()
            )

            document.set(newMessage).await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
