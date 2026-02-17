package com.example.workout.screens.details.state

import com.example.domain.model.Exercise
import com.example.domain.model.Set
import com.example.domain.model.Workout

data class WorkoutDetailsUIState(
    val isLoading: Boolean = true,
    val date: String = "",
    val exerciseList: List<Exercise>
) {
    companion object{
        val set = Set(
            id = 0,
            count = 1,
            weight = "55",
            reps = "12"
        )

        val exercise = Exercise(
            id = 0,
            name = "Bench Press",
            sets = listOf(set, set)
        )

        fun initial() = WorkoutDetailsUIState(isLoading = true, exerciseList = emptyList())

        fun preview() : WorkoutDetailsUIState{
            return WorkoutDetailsUIState(
                isLoading = false,
                exerciseList = listOf(exercise, exercise)
            )
        }
    }
}