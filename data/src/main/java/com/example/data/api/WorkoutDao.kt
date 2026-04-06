package com.example.data.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.data.model.ExerciseEntity
import com.example.data.model.ExerciseWithSets
import com.example.data.model.SetEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutWithExercises
import com.example.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Transaction
    @Query("SELECT * FROM workouts")
    fun getWorkoutsFlow(): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM exercises WHERE workoutOwnerId = :workoutId")
    fun getExercisesFlow(workoutId: Int): Flow<List<ExerciseWithSets>>

    @Transaction
    @Query("SELECT * FROM workouts")
    suspend fun getWorkouts(): List<WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :id")
    suspend fun getWorkout(id: Int): WorkoutWithExercises?

    @Query("DELETE FROM workouts WHERE workoutId = :id")
    suspend fun deleteWorkout(id: Int)

    @Insert
    suspend fun insertWorkoutEntity(workout: WorkoutEntity): Long

    @Insert
    suspend fun insertExerciseEntity(exercise: ExerciseEntity): Long

    @Insert
    suspend fun insertSetEntity(sets: List<SetEntity>)

    @Upsert
    suspend fun upsertWorkoutEntity(workout: WorkoutEntity)

    @Query("DELETE FROM exercises WHERE workoutOwnerId = :workoutId")
    suspend fun deleteExercisesByWorkoutId(workoutId: Int)
}