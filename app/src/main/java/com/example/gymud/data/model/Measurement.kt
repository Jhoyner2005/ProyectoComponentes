package com.example.gymud.data.model

import com.google.firebase.Timestamp

data class Measurement(
    val id: String = "",
    val userId: String = "",
    val date: Timestamp = Timestamp.now(),
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val muscleMass: Double = 0.0,
    val bodyFat: Double = 0.0,
    val bmi: Double = 0.0, // Índice de Masa Corporal
    val imm: Double = 0.0  // Índice de Masa Muscular
)