package com.example.data.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.model.ExerciseEntity
import com.example.data.model.SetEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutWithExercises
import com.example.data.model.toEntity
import com.example.domain.model.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Transaction
    @Query("SELECT * FROM workouts")
    fun getWorkoutsFlow(): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM workouts")
    suspend fun getWorkouts(): List<WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :id")
    suspend fun getWorkout(id: Int): WorkoutWithExercises?

    @Insert
    suspend fun insertWorkoutEntity(workout: WorkoutEntity): Long

    @Insert
    suspend fun insertExerciseEntity(exercise: ExerciseEntity): Long

    @Insert
    suspend fun insertSetEntity(sets: List<SetEntity>)
}
