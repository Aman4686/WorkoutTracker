package com.example.workout.screens.exersices.state

interface ExerciseListSideEffect {
    data object NavigateBack : ExerciseListSideEffect
    data class ShowToast(val message: String) : ExerciseListSideEffect
}