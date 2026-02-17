package com.example.domain.repository

import com.example.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun getWorkouts(): Flow<List<Workout>>

    suspend fun getWorkout(id: Int): Workout?

    suspend fun addWorkout(workout: Workout)
}