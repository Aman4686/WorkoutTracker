package com.example.workout.screens.list.state

import com.example.domain.model.Workout
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class WorkoutListUIState(
    val workoutsList: ImmutableList<Workout> = emptyList<Workout>().toImmutableList(),
    val isLoading: Boolean = false,
){
    companion object{
        fun initial() = WorkoutListUIState(isLoading = false)

        fun preview() = WorkoutListUIState(
            workoutsList = listOf(Workout.preview(), Workout.preview()).toImmutableList(),
            isLoading = false
        )
    }
}