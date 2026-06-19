package com.example.workout.screens.list.state

interface WorkoutListUIAction {
    data object AddNewWorkout : WorkoutListUIAction
    data class DeleteWorkout(val workoutId: Int) : WorkoutListUIAction
}