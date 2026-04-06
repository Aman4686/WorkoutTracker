package com.example.data.model.mappers

import com.example.data.model.ExerciseTypeEntity
import com.example.data.model.ExerciseWithSets
import com.example.data.model.ExerciseEntity
import com.example.domain.model.ExerciseType
import com.example.domain.model.Exercise

fun ExerciseWithSets.mapToDomain(): Exercise {
    return Exercise(
        id = exercise.exerciseId,
        type = type.toDomain(),
        sets = sets.map { it.mapToDomain() }
    )
}

fun Exercise.toEntity(workoutId: Int): ExerciseEntity {
    return ExerciseEntity(
        workoutOwnerId = workoutId,
        exerciseTypeOwnerId = type.id
    )
}