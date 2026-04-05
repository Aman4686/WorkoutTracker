package com.example.data.model.mappers

import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutWithExersices
import com.example.domain.model.Workout

fun WorkoutWithExersices.mapToDomain(): Workout {
    return Workout(
        id = workout.workoutId,
        date = workout.date,
        exersiceList = exersices.map { it.mapToDomain() }
    )
}

fun Workout.toEntity(): WorkoutEntity {
    return WorkoutEntity(
        workoutId = id,
        date = date
    )
}
