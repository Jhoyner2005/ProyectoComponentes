package com.example.gymud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymud.data.model.Measurement
import com.example.gymud.data.repository.ProgressRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class)
class ProgressViewModel : ViewModel() {
    private val repository = ProgressRepository()

    private val _currentUserId = MutableStateFlow("")

    val measurements: StateFlow<List<Measurement>> = _currentUserId.flatMapLatest { uid ->
        repository.getMeasurementsStream(uid)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadUserMeasurements(userId: String) {
        _currentUserId.value = userId
    }

    fun saveMeasurement(
        userId: String,
        weightStr: String,
        heightStr: String,
        muscleMassStr: String,
        bodyFatStr: String
    ) {
        val weight = weightStr.toDoubleOrNull()
        val heightCm = heightStr.toDoubleOrNull()
        val muscleMass = muscleMassStr.toDoubleOrNull()
        val bodyFat = bodyFatStr.toDoubleOrNull()

        if (weight == null || heightCm == null || muscleMass == null || bodyFat == null || weight <= 0 || heightCm <= 0) {
            _message.value = "Por favor ingresa valores numéricos válidos"
            return
        }

        val heightM = heightCm / 100.0
        val rawBmi = weight / (heightM * heightM)
        val bmi = (rawBmi * 10.0).roundToInt() / 10.0 // Redondear a 1 decimal

        val rawImm = if (weight > 0) (muscleMass / weight) * 100.0 else 0.0
        val imm = (rawImm * 10.0).roundToInt() / 10.0

        val measurement = Measurement(
            date = Timestamp.now(),
            weight = weight,
            height = heightCm,
            muscleMass = muscleMass,
            bodyFat = bodyFat,
            bmi = bmi,
            imm = imm
        )

        _isLoading.value = true

        viewModelScope.launch {
            repository.addMeasurement(userId, measurement).fold(
                onSuccess = {
                    _isLoading.value = false
                    _message.value = "¡Registro de progreso guardado con éxito!"
                },
                onFailure = { error ->
                    _isLoading.value = false
                    _message.value = "Error al guardar: ${error.localizedMessage}"
                }
            )
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}