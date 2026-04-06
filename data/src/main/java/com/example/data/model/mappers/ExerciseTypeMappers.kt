package com.example.data.model.mappers

import com.example.data.model.ExerciseTypeEntity
import com.example.domain.model.ExerciseType

fun ExerciseType.toEntity(): ExerciseTypeEntity {
    return ExerciseTypeEntity(
        name = name
    )
}

fun ExerciseTypeEntity.toDomain(): ExerciseType {
    return ExerciseType(
        id = exerciseTypeId,
        name = name
    )
}