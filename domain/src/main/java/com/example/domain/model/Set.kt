package com.example.domain.model

data class Set(
    val id: Int = 0,
    val count: Int,
    val weight: String,
    val reps: String
) {
    companion object{
        fun preview(): Set = Set(
            id = 0,
            count = 1,
            weight = "55",
            reps = "12"
        )

        fun new(count: Int): Set = Set(
            id = 0,
            count = count,
            weight = "0",
            reps = "0"

        )
    }
}