package com.example.domain.model

data class Set(
    val id: Int = 0,
    val weight: String,
    val reps: String
) {
    companion object{
        fun new(): Set = Set(
            id = 0,
            weight = "0",
            reps = "0"

        )
    }
}