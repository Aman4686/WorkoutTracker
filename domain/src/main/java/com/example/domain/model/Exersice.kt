package com.example.domain.model

data class Exercise(
    val id: Int = 0,
    val type: ExerciseType,
    val sets: List<Set> = emptyList()
)