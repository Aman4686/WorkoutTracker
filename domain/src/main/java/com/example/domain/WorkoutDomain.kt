package com.example.domain

import com.example.domain.di.IoDispatcher
import com.example.domain.model.ExerciseType
import com.example.domain.model.Exersice
import com.example.domain.model.Workout
import com.example.domain.repository.ExerciseTypeRepository
import com.example.domain.repository.WorkoutRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface WorkoutDomain {
    fun getWorkoutsFlow(): Flow<List<Workout>>

    fun getExersiceTypeFlow(): Flow<List<ExerciseType>>

    suspend fun getWorkouts(): List<Workout>

    suspend fun getWorkout(id: Int): Workout?

    suspend fun addWorkout(workout: Workout)

    suspend fun deleteWorkout(id: Int)

    suspend fun insertOrUpdateWorkout(workout: Workout)


}

class WorkoutDomainImpl @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseTypeRepository: ExerciseTypeRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : WorkoutDomain {

    override fun getWorkoutsFlow(): Flow<List<Workout>> {
        return workoutRepository.getWorkoutsFlow()
    }

    override fun getExersiceTypeFlow(): Flow<List<ExerciseType>> {
        return exerciseTypeRepository.getExersiceTypeFlow()
    }

    override suspend fun getWorkouts(): List<Workout> {
        return workoutRepository.getWorkouts()
    }

    override suspend fun getWorkout(id: Int): Workout? {
        return workoutRepository.getWorkout(id)
    }

    override suspend fun addWorkout(workout: Workout) {
        return workoutRepository.addWorkout(workout)
    }

    override suspend fun deleteWorkout(id: Int) {
        return workoutRepository.deleteWorkout(id)
    }

    override suspend fun insertOrUpdateWorkout(workout: Workout) {
        return if (workout.id == 0)
            workoutRepository.addWorkout(workout)
        else
            workoutRepository.putWorkout(workout)
    }

}