package com.example.domain.model

data class Exersice(
    val id: Int = 0,
    val type: ExerciseType,
    val sets: List<Set>
) {
    companion object{

        fun preview(): Exersice = Exersice(
            type = ExerciseType(
                name = "Bench Press"
            ),
            sets = listOf(Set.preview(), Set.preview())
        )
    }
}

private fun Exersice.updateSet(updated: Set): Exersice {
    return copy(
        sets = sets.map {
            if (it.id == updated.id) updated else it
        }
    )
}