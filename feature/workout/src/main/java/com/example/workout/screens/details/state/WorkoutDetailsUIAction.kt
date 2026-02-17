package com.example.workout.screens.details.state

import com.example.domain.model.Exercise
import com.example.domain.model.Set

sealed interface WorkoutDetailsUIAction {
    data class AddSet(val exerciseId: Int) : WorkoutDetailsUIAction
    data class AddExercise(val exercise: Exercise) : WorkoutDetailsUIAction
    data object SaveWorkout : WorkoutDetailsUIAction
}