
package com.example.gymud.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymud.data.model.Measurement
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date
import kotlin.math.roundToInt

class ProgressViewModel : ViewModel() {

    private val _measurements = MutableStateFlow(createSampleMeasurements())
    val measurements: StateFlow<List<Measurement>> = _measurements

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Load local measurements
    fun loadUserMeasurements(userId: String) {
        _measurements.value = _measurements.value.map {
            it.copy(userId = userId)
        }
    }

    // Save a new physical measurement
    fun saveMeasurement(
        userId: String,
        weightStr: String,
        heightStr: String,
        muscleMassStr: String,
        bodyFatStr: String
    ) {
        val weight = weightStr.replace(",", ".").toDoubleOrNull()
        val heightCm = heightStr.replace(",", ".").toDoubleOrNull()
        val muscleMass = muscleMassStr.replace(",", ".").toDoubleOrNull()
        val bodyFat = bodyFatStr.replace(",", ".").toDoubleOrNull()

        if (weight == null || heightCm == null ||
            muscleMass == null || bodyFat == null
        ) {
            _message.value = "Por favor ingresa valores numéricos válidos"
            return
        }

        if (weight <= 0 || heightCm <= 0 ||
            muscleMass < 0 || bodyFat !in 0.0..100.0
        ) {
            _message.value = "Verifica que los valores ingresados sean válidos"
            return
        }

        if (muscleMass > weight) {
            _message.value = "La masa muscular no puede superar el peso total"
            return
        }

        val heightM = heightCm / 100.0
        val bmi = ((weight / (heightM * heightM)) * 10).roundToInt() / 10.0
        val imm = ((muscleMass / weight) * 1000).roundToInt() / 10.0

        val newMeasurement = Measurement(
            id = System.currentTimeMillis().toString(),
            userId = userId,
            date = Timestamp.now(),
            weight = weight,
            height = heightCm,
            muscleMass = muscleMass,
            bodyFat = bodyFat,
            bmi = bmi,
            imm = imm
        )

        _isLoading.value = true

        _measurements.value = listOf(newMeasurement) + _measurements.value
        _message.value = "¡Registro de progreso guardado con éxito!"
        _isLoading.value = false
    }

    // Clear the current message
    fun clearMessage() {
        _message.value = null
    }

    // Create sample measurements for the screen
    private fun createSampleMeasurements(): List<Measurement> {
        val currentDate = System.currentTimeMillis()

        return listOf(
            createMeasurement(
                id = "sample_1",
                daysAgo = 0,
                weight = 71.2,
                muscleMass = 30.0,
                bodyFat = 19.5
            ),
            createMeasurement(
                id = "sample_2",
                daysAgo = 15,
                weight = 71.8,
                muscleMass = 29.5,
                bodyFat = 20.0
            ),
            createMeasurement(
                id = "sample_3",
                daysAgo = 30,
                weight = 72.5,
                muscleMass = 29.0,
                bodyFat = 21.0
            )
        ).map {
            it.copy(
                date = Timestamp(
                    Date(currentDate - (it.date.seconds * 1000))
                )
            )
        }
    }

    private fun createMeasurement(
        id: String,
        daysAgo: Int,
        weight: Double,
        muscleMass: Double,
        bodyFat: Double
    ): Measurement {
        val height = 175.0
        val heightM = height / 100.0
        val bmi = ((weight / (heightM * heightM)) * 10).roundToInt() / 10.0
        val imm = ((muscleMass / weight) * 1000).roundToInt() / 10.0

        return Measurement(
            id = id,
            userId = "demo_user",
            date = Timestamp(Date(daysAgo * 24L * 60 * 60 * 1000)),
            weight = weight,
            height = height,
            muscleMass = muscleMass,
            bodyFat = bodyFat,
            bmi = bmi,
            imm = imm
        )
    }
}
