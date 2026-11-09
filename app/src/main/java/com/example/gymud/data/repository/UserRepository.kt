
package com.example.gymud.data.repository

import com.example.gymud.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Listen to registered trainers in real time
    fun getTrainersStream(): Flow<List<User>> = callbackFlow {

        val listener = firestore.collection("users")
            .whereEqualTo("role", "TRAINER")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val trainers = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(User::class.java)?.copy(
                        uid = document.id
                    )
                } ?: emptyList()

                trySend(trainers)
            }

        awaitClose {
            listener.remove()
        }
    }
}
