package com.example.domain.model

data class Exercise(
    val id: Int = 0,
    val type: ExerciseType,
    val sets: List<Set> = emptyList()
) {
    companion object{

        fun preview(): Exercise = Exercise(
            type = ExerciseType(
                name = "Bench Press"
            ),
            sets = listOf(Set.preview(), Set.preview())
        )
    }
}