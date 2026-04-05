package com.example.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "exersices",
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
            childColumns = ["exersiceTypeOwnerId"],
            onDelete = ForeignKey.RESTRICT
        )

    ],
    indices = [
        Index("workoutOwnerId"),
        Index("exersiceTypeOwnerId")]
)

data class ExersiceEntity(
    @PrimaryKey(autoGenerate = true)
    val exersiceId: Int = 0,
    val exersiceTypeOwnerId: Int = 0,

    val workoutOwnerId: Int,

    )

data class ExersiceWithSets(
    @Embedded val exersice: ExersiceEntity,

    @Relation(
        parentColumn = "exersiceId",
        entityColumn = "exersiceOwnerId"
    )
    val sets: List<SetEntity>,

    @Relation(
        parentColumn = "exersiceTypeOwnerId",
        entityColumn = "exerciseTypeId"
    )
    val type: ExerciseTypeEntity
)

