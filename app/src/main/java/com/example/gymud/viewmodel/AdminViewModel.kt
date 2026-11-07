package com.example.gymud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymud.data.model.Inventory
import com.example.gymud.data.model.User
import com.example.gymud.data.repository.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
    private val repository = AdminRepository()

    val users: StateFlow<List<User>> = repository.getUsersStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inventory: StateFlow<List<Inventory>> = repository.getInventoryStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeUsersCount: StateFlow<Int> = users.map { list ->
        list.count { it.isMembershipActive }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMonthlyIncome: StateFlow<Double> = users.map { list ->
        list.sumOf { it.lastPaymentAmount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun updateUserRoleDirect(uid: String, newRole: String) {
        viewModelScope.launch {
            repository.updateUserRole(uid, newRole).fold(
                onSuccess = { _message.value = "Rol actualizado a $newRole" },
                onFailure = { _message.value = "Error al actualizar rol: ${it.localizedMessage}" }
            )
        }
    }

    fun updateUserRole(uid: String, currentRole: String) {
        val newRole = when (currentRole) {
            "USER" -> "TRAINER"
            "TRAINER" -> "ADMIN"
            else -> "USER"
        }
        updateUserRoleDirect(uid, newRole)
    }

    fun toggleUserMembership(uid: String, currentActive: Boolean) {
        viewModelScope.launch {
            repository.toggleUserMembership(uid, !currentActive).fold(
                onSuccess = { _message.value = if (!currentActive) "Membresía Activada" else "Membresía Desactivada" },
                onFailure = { _message.value = "Error: ${it.localizedMessage}" }
            )
        }
    }

    fun addInventoryItem(name: String, category: String) {
        if (name.isBlank() || category.isBlank()) {
            _message.value = "El nombre y categoría son requeridos"
            return
        }
        viewModelScope.launch {
            repository.addInventoryItem(name, category).fold(
                onSuccess = { _message.value = "Equipo agregado al inventario" },
                onFailure = { _message.value = "Error al agregar equipo: ${it.localizedMessage}" }
            )
        }
    }

    fun toggleInventoryStatus(itemId: String, currentIsDamaged: Boolean) {
        viewModelScope.launch {
            repository.toggleInventoryStatus(itemId, !currentIsDamaged).fold(
                onSuccess = { _message.value = if (!currentIsDamaged) "Equipo reportado como Dañado" else "Equipo reparado y Operativo" },
                onFailure = { _message.value = "Error al cambiar estado: ${it.localizedMessage}" }
            )
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}