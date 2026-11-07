package com.example.gymud.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Inventory(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    @get:PropertyName("isDamaged") @set:PropertyName("isDamaged") @field:PropertyName("isDamaged")
    var isDamaged: Boolean = false,
    val reportDetail: String = "",
    val nextMaintenanceDate: Timestamp? = null
)