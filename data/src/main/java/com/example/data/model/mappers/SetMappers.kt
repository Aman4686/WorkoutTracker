package com.example.data.model.mappers

import com.example.data.model.SetEntity
import com.example.domain.model.Set

fun SetEntity.mapToDomain(): Set {
    return Set(
        id = setId,
        count = count,
        reps = reps,
        weight = weight
    )
}

fun Set.toEntity(exerciseId: Int): SetEntity {
    return SetEntity(
        setId = id,
        exerciseOwnerId = exerciseId,
        count = count,
        weight = weight,
        reps = reps
    )
}
