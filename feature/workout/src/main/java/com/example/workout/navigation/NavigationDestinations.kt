package com.example.workout.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {

    @Serializable
    data object WorkoutList : Route, NavKey

    @Serializable
    data class WorkoutDetails(val workoutId: Int = -1) : Route, NavKey

    @Serializable
    data class ExerciseListSelector(val workoutId: Int) : Route, NavKey
}