package com.example.workout.screens.details.state

import com.example.domain.model.Exersice
import com.example.domain.model.Set

sealed interface WorkoutDetailsUIAction {
    data class UpdateSet(val exersiceId: Int, val set: Set) : WorkoutDetailsUIAction
    data class UpdateExersice(val exersice: Exersice) : WorkoutDetailsUIAction
    data class AddSet(val exersiceId: Int) : WorkoutDetailsUIAction
    data class AddExersice(val exersice: Exersice) : WorkoutDetailsUIAction
    data object SaveWorkout : WorkoutDetailsUIAction
    data object DeleteWorkout : WorkoutDetailsUIAction
}