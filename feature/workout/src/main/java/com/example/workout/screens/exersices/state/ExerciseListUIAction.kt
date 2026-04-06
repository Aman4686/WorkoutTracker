package com.example.workout.screens.exercises.state

sealed interface ExerciseListUIAction{
    data class SelectExercise(val exerciseId: Int) : ExerciseListUIAction
    data class AddExercise(val exerciseName: String) : ExerciseListUIAction
}
