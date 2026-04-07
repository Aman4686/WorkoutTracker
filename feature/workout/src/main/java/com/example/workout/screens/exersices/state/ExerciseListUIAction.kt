package com.example.workout.screens.exersices.state

sealed interface ExerciseListUIAction{
    data class SelectExercise(val exerciseId: Int) : ExerciseListUIAction
    data class AddNewExerciseType(val exerciseName: String) : ExerciseListUIAction
    data object SaveExerciseToWorkout : ExerciseListUIAction
}
