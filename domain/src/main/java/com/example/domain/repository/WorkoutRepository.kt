package com.example.domain.repository

import com.example.domain.model.Exercise
import com.example.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkoutsFlow(): Flow<List<Workout>>

    fun getExercisesFlow(workoutId: Int): Flow<List<Exercise>>

    suspend fun getWorkouts(): List<Workout>

    suspend fun getWorkout(id: Int): Workout?

    suspend fun addWorkout(workout: Workout): Int

    suspend fun putWorkout(workout: Workout)

    suspend fun deleteWorkout(id: Int)
}