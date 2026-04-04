package com.example.domain.repository

import com.example.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkoutsFlow(): Flow<List<Workout>>

    suspend fun getWorkouts(): List<Workout>

    suspend fun getWorkout(id: Int): Workout?

    suspend fun addWorkout(workout: Workout)

    suspend fun putWorkout(workout: Workout)

    suspend fun deleteWorkout(id: Int)
}