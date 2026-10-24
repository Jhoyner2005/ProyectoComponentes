package com.example.gymud.data.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "USER",
    val isMembershipActive: Boolean = false,
    val membershipEndDate: Timestamp? = null,
    val lastPaymentAmount: Double = 0.0
)