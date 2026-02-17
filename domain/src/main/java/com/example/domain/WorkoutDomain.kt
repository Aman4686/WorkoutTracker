package com.example.domain

import com.example.domain.di.IoDispatcher
import com.example.domain.model.Workout
import com.example.domain.repository.WorkoutRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

interface WorkoutDomain{
    suspend fun getWorkouts(): List<Workout>
    suspend fun getWorkout(id: Int): Workout
}

class WorkoutDomainImpl @Inject constructor(
    workoutRepository: WorkoutRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : WorkoutDomain{
    override suspend fun getWorkouts(): List<Workout> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkout(id: Int): Workout {
        TODO("Not yet implemented")
    }

}