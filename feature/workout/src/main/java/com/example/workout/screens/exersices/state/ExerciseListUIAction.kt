package com.example.workout.screens.exersices.state

sealed interface ExerciseListUIAction{
    data class SelectExercise(val exerciseId: Int) : ExerciseListUIAction
}
