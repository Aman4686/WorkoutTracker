package com.example.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Int = 0,
    val date: String
)

data class WorkoutWithExersices(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        entity = ExersiceEntity::class,
        parentColumn = "workoutId",
        entityColumn = "workoutOwnerId"
    )
    val exersices: List<ExersiceWithSets>
)
