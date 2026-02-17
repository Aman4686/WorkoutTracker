package com.example.domain.model

data class Workout(
    val id: Int = 0,
    val date: String,
    val exerciseList: List<Exercise>
)