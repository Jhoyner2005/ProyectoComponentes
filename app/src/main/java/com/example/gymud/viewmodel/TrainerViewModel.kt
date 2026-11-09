
package com.example.gymud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymud.data.model.User
import com.example.gymud.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrainerViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _trainers = MutableStateFlow<List<User>>(emptyList())
    val trainers: StateFlow<List<User>> = _trainers.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadTrainers()
    }

    // Load available trainers from Firebase
    private fun loadTrainers() {
        viewModelScope.launch {
            try {
                repository.getTrainersStream().collect { trainerList ->
                    _trainers.value = trainerList
                    _isLoading.value = false
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Could not load trainers: ${e.message}"
            }
        }
    }
}
