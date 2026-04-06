package com.example.data.model.mappers

import com.example.data.model.SetEntity
import com.example.domain.model.Set

fun SetEntity.mapToDomain(): Set {
    return Set(
        count = count,
        reps = reps,
        weight = weight
    )
}

fun Set.toEntity(exerciseId: Int): SetEntity {
    return SetEntity(
        exerciseOwnerId = exerciseId,
        count = count,
        weight = weight,
        reps = reps
    )
}
