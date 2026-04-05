package com.example.data.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.data.model.ExersiceEntity
import com.example.data.model.SetEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutWithExersices
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Transaction
    @Query("SELECT * FROM workouts")
    fun getWorkoutsFlow(): Flow<List<WorkoutWithExersices>>

    @Transaction
    @Query("SELECT * FROM workouts")
    suspend fun getWorkouts(): List<WorkoutWithExersices>

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :id")
    suspend fun getWorkout(id: Int): WorkoutWithExersices?

    @Query("DELETE FROM workouts WHERE workoutId = :id")
    suspend fun deleteWorkout(id: Int)

    @Insert
    suspend fun insertWorkoutEntity(workout: WorkoutEntity): Long

    @Insert
    suspend fun insertExersiceEntity(exersice: ExersiceEntity): Long

    @Insert
    suspend fun insertSetEntity(sets: List<SetEntity>)

    @Upsert
    suspend fun upsertWorkoutEntity(workout: WorkoutEntity)

    @Query("DELETE FROM exersices WHERE workoutOwnerId = :workoutId")
    suspend fun deleteExersicesByWorkoutId(workoutId: Int)
}