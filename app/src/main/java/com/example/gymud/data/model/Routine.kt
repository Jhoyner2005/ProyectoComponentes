package com.example.gymud.data.model

data class Routine(
    val id: String = "",
    val userId: String = "",
    val trainerId: String = "",
    val title: String = "",
    val category: String = "",
    val exercises: List<Exercise> = emptyList()
)

data class Exercise(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val sets: Int = 0,
    val reps: Int = 0
)