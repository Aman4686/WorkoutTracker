package com.example.domain.model

data class Exercise(
    val id: Int = 0,
    val name: String,
    val sets: List<Set>
) {
    companion object{


        fun preview(): Exercise = Exercise(
            id = 0,
            name = "Bench Press",
            sets = listOf(Set.preview(), Set.preview())
        )
    }
}