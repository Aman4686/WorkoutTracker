package com.example.data.model.mappers

import com.example.data.model.ExerciseTypeEntity
import com.example.data.model.ExersiceWithSets
import com.example.data.model.ExersiceEntity
import com.example.domain.model.ExerciseType
import com.example.domain.model.Exersice

fun ExersiceWithSets.mapToDomain(): Exersice {
    return Exersice(
        id = exersice.exersiceId,
        type = type.mapToDomain(),
        sets = sets.map { it.mapToDomain() }
    )
}

fun Exersice.toEntity(workoutId: Int): ExersiceEntity {
    return ExersiceEntity(
        workoutOwnerId = workoutId,
        exersiceTypeOwnerId = type.id
    )
}

fun ExerciseTypeEntity.mapToDomain() : ExerciseType {
    return ExerciseType(
        id = exerciseTypeId,
        name = name
    )
}

fun ExerciseType.toEntity(): ExerciseTypeEntity {
    return ExerciseTypeEntity(
        name = name
    )
}
