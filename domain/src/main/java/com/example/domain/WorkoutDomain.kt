package com.example.domain

import com.example.domain.di.IoDispatcher
import com.example.domain.model.Workout
import com.example.domain.repository.WorkoutRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface WorkoutDomain{
    fun getWorkoutsFlow(): Flow<List<Workout>>

    suspend fun getWorkouts(): List<Workout>

    suspend fun getWorkout(id: Int): Workout?

    suspend fun addWorkout(workout: Workout)
}

class WorkoutDomainImpl @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : WorkoutDomain{
    override fun getWorkoutsFlow(): Flow<List<Workout>> {
        return workoutRepository.getWorkoutsFlow()
    }

    override suspend fun getWorkouts(): List<Workout>{
        return workoutRepository.getWorkouts()
    }

    override suspend fun getWorkout(id: Int): Workout?{
        return workoutRepository.getWorkout(id)
    }

    override suspend fun addWorkout(workout: Workout){
        return workoutRepository.addWorkout(workout)
    }

}