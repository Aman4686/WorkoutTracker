package com.example.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["workoutId"],
            childColumns = ["workoutOwnerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseTypeEntity::class,
            parentColumns = ["exerciseTypeId"],
            childColumns = ["exerciseTypeOwnerId"],
            onDelete = ForeignKey.RESTRICT
        )

    ],
    indices = [
        Index("workoutOwnerId"),
        Index("exerciseTypeOwnerId")]
)

data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int = 0,

    val exerciseTypeOwnerId: Int = 0,
    val workoutOwnerId: Int,

    )

data class ExerciseWithSets(
    @Embedded val exercise: ExerciseEntity,

    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseOwnerId"
    )
    val sets: List<SetEntity>,

    @Relation(
        parentColumn = "exerciseTypeOwnerId",
        entityColumn = "exerciseTypeId"
    )
    val type: ExerciseTypeEntity
)

