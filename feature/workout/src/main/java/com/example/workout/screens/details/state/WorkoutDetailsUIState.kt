package com.example.workout.screens.details.state

import com.example.domain.model.Exercise

data class WorkoutDetailsUIState(
    val isLoading: Boolean = true,
    val date: String = "",
    val name: String = "",
    val exerciseList: List<Exercise>
) {

    companion object{
        fun initial() = WorkoutDetailsUIState(isLoading = true, exerciseList = emptyList())

        fun preview() : WorkoutDetailsUIState{
            return WorkoutDetailsUIState(
                isLoading = false,
                exerciseList = listOf(Exercise.preview(), Exercise.preview())
            )
        }
    }
}