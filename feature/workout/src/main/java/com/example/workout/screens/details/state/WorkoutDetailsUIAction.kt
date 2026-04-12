package com.example.workout.screens.details.state

import com.example.domain.model.Exercise
import com.example.domain.model.Set

sealed interface WorkoutDetailsUIAction {
    data class UpdateSet(val exerciseId: Int, val set: SetUIModel) : WorkoutDetailsUIAction
    data class AddSet(val exerciseId: Int, val count: Int) : WorkoutDetailsUIAction
    data object DeleteWorkout : WorkoutDetailsUIAction
}