package com.example.gymud.data.model

import com.google.firebase.Timestamp

data class Finances(
    val id: String = "",
    val type: String = "INCOME", // Puede ser "INCOME" (Ingreso) o "EXPENSE" (Gasto)
    val amount: Double = 0.0,
    val concept: String = "",
    val date: Timestamp = Timestamp.now()
)