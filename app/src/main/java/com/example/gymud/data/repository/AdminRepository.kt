package com.example.gymud.data.repository

import com.example.gymud.data.model.Inventory
import com.example.gymud.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AdminRepository {
    private val firestore = FirebaseFirestore.getInstance()

    // Escuchar lista de usuarios en tiempo real
    fun getUsersStream(): Flow<List<User>> = callbackFlow {
        val listener = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val users = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(User::class.java)
                } ?: emptyList()
                trySend(users)
            }
        awaitClose { listener.remove() }
    }

    // Actualizar rol de un usuario
    suspend fun updateUserRole(uid: String, newRole: String): Result<Unit> {
        return try {
            firestore.collection("users").document(uid)
                .update("role", newRole)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Activar o desactivar membresía
    suspend fun toggleUserMembership(uid: String, isActive: Boolean): Result<Unit> {
        return try {
            firestore.collection("users").document(uid)
                .update(
                    mapOf(
                        "membershipActive" to isActive,
                        "isMembershipActive" to isActive
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Escuchar inventario en tiempo real
    fun getInventoryStream(): Flow<List<Inventory>> = callbackFlow {
        val listener = firestore.collection("inventory")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Inventory::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

    // Agregar nuevo equipo al inventario
    suspend fun addInventoryItem(name: String, category: String): Result<Unit> {
        return try {
            val docRef = firestore.collection("inventory").document()
            val newItem = Inventory(
                id = docRef.id,
                name = name,
                category = category,
                isDamaged = false,
                reportDetail = ""
            )
            docRef.set(newItem).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Cambiar estado de equipo (Dañado / Operativo) con reporte detallado
    suspend fun reportInventoryDamage(itemId: String, isDamaged: Boolean, reportDetail: String = ""): Result<Unit> {
        return try {
            firestore.collection("inventory").document(itemId)
                .set(
                    mapOf(
                        "isDamaged" to isDamaged,
                        "reportDetail" to reportDetail
                    ),
                    SetOptions.merge()
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleInventoryStatus(itemId: String, isDamaged: Boolean): Result<Unit> {
        return reportInventoryDamage(itemId, isDamaged, "")
    }
}