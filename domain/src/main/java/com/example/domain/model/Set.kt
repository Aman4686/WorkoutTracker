package com.example.domain.model

data class Set(
    val id: Int = 0,
    val count: Int,
    val weight: String,
    val reps: String
) {
    companion object{
        fun new(count: Int): Set = Set(
            id = 0,
            count = count,
            weight = "0",
            reps = "0"

        )
    }
}