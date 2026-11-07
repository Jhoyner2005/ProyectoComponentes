package com.example.gymud.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "USER",
    @get:PropertyName("membershipActive") @set:PropertyName("membershipActive") @field:PropertyName("membershipActive")
    var isMembershipActive: Boolean = false,
    val membershipEndDate: Timestamp? = null,
    val lastPaymentAmount: Double = 0.0
)