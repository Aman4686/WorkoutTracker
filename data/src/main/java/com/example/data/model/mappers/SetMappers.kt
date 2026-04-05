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

fun Set.toEntity(exersiceId: Int): SetEntity {
    return SetEntity(
        exersiceOwnerId = exersiceId,
        count = count,
        weight = weight,
        reps = reps
    )
}
