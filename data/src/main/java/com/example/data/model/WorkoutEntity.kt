package com.example.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.domain.model.Workout

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

fun WorkoutWithExercises.mapToDomain(): Workout{
    return Workout(
        id = workout.workoutId,
        date = workout.date,
        exerciseList = exercises.map { it.mapToDomain() }
    )
}

fun Workout.toEntity(): WorkoutEntity {
    return WorkoutEntity(
        date = date
    )
}

