package com.example.gymud.data.model

import com.google.firebase.Timestamp

data class Inventory(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val isDamaged: Boolean = false,
    val nextMaintenanceDate: Timestamp? = null
)