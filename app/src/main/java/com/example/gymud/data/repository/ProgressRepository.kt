package com.example.gymud.data.repository

import com.example.gymud.data.model.Measurement
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProgressRepository {
    private val firestore = FirebaseFirestore.getInstance()

    // Escuchar el historial de mediciones del usuario en tiempo real
    fun getMeasurementsStream(userId: String): Flow<List<Measurement>> = callbackFlow {
        if (userId.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = firestore.collection("users")
            .document(userId)
            .collection("measurements")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Measurement::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(items)
            }

        awaitClose { listener.remove() }
    }

    // Agregar un nuevo registro de medición física
    suspend fun addMeasurement(userId: String, measurement: Measurement): Result<Unit> {
        return try {
            val docRef = firestore.collection("users")
                .document(userId)
                .collection("measurements")
                .document()

            val newMeasurement = measurement.copy(id = docRef.id, userId = userId)
            docRef.set(newMeasurement).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}