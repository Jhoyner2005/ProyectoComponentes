package com.example.gymud.data.repository

import com.example.gymud.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<String> {
        return try {
            // 1. Crear credenciales en Firebase Authentication
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Error al obtener el identificador de usuario")

            // 2. Construir el objeto de perfil del usuario
            val newUser = User(
                uid = uid,
                name = name,
                email = email,
                role = "USER", // Por defecto todos son clientes, el admin podrá cambiarlo luego
                isMembershipActive = false
            )

            // 3. Guardar el perfil en la colección "users" de Firestore
            firestore.collection("users").document(uid).set(newUser).await()

            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(uid: String): Result<User> {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            val user = doc.toObject(User::class.java)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Usuario no encontrado en la base de datos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}