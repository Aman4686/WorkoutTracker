package com.example.data.model.mappers

import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutWithExercises
import com.example.domain.model.Workout

fun WorkoutWithExercises.mapToDomain(): Workout {
    return Workout(
        id = workout.workoutId,
        date = workout.date,
        exerciseList = exercises.map { it.mapToDomain() }
    )
}

fun Workout.toEntity(): WorkoutEntity {
    return WorkoutEntity(
        workoutId = id,
        date = date
    )
}
