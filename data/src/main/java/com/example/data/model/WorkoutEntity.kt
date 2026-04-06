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

data class WorkoutWithExercises(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        entity = ExerciseEntity::class,
        parentColumn = "workoutId",
        entityColumn = "workoutOwnerId"
    )
    val exercises: List<ExerciseWithSets>
)
