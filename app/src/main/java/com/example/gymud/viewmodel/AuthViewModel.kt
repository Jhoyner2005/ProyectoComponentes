package com.example.gymud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymud.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val uid: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Los campos no pueden estar vacíos")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            repository.login(email, password).fold(
                onSuccess = { uid -> _authState.value = AuthState.Success(uid) },
                onFailure = { error -> _authState.value = AuthState.Error(error.localizedMessage ?: "Error de autenticación") }
            )
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Todos los campos son obligatorios")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            repository.register(name, email, password).fold(
                onSuccess = { uid -> _authState.value = AuthState.Success(uid) },
                onFailure = { error -> _authState.value = AuthState.Error(error.localizedMessage ?: "Error al registrar la cuenta") }
            )
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}