package com.example.workout.screens.list.state

interface WorkoutListSideEffect {
    data class NavigateToWorkoutDetails(val workoutId: Int) : WorkoutListSideEffect
}