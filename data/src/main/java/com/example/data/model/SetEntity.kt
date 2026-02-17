package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.domain.model.Set

@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseOwnerId")]
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0,

    val exerciseOwnerId: Int, // FK to Exercise

    val count: Int,
    val weight: String,
    val reps: String
)

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
