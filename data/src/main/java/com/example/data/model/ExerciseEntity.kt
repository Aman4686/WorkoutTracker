package com.example.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.domain.model.Exercise
import com.example.domain.model.Workout

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["workoutId"],
            childColumns = ["workoutOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutOwnerId")]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int = 0,

    val workoutOwnerId: Int,
    val name: String
)

data class ExerciseWithSets(
    @Embedded val exercise: ExerciseEntity,

    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseOwnerId"
    )
    val sets: List<SetEntity>
)

fun ExerciseWithSets.mapToDomain(): Exercise{
    return Exercise(
        id = exercise.exerciseId,
        name = exercise.name,
        sets = sets.map { it.mapToDomain() }
    )
}

fun Exercise.toEntity(workoutId: Int): ExerciseEntity {
    return ExerciseEntity(
        workoutOwnerId = workoutId,
        name = name
    )
}

