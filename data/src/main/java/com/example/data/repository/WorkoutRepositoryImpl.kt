package com.example.data.repository

import androidx.room.Transaction
import com.example.data.api.WorkoutDao
import com.example.data.model.mapToDomain
import com.example.data.model.toEntity
import com.example.domain.di.IoDispatcher
import com.example.domain.model.Workout
import com.example.domain.repository.WorkoutRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : WorkoutRepository {

    override suspend fun getWorkouts(): Flow<List<Workout>> {
        return workoutDao.getWorkouts()
            .map { workoutWithExercisesList ->
                workoutWithExercisesList.map { it.mapToDomain() }
            }

    }

    override suspend fun getWorkout(id: Int): Workout? {
        return workoutDao.getWorkout(id = id)?.mapToDomain()
    }

    @Transaction
    override suspend fun addWorkout(workout: Workout) {

        val workoutId = workoutDao.insertWorkoutEntity(
            workout.toEntity()
        ).toInt()

        workout.exerciseList.forEach { exercise ->

            val exerciseId = workoutDao.insertExerciseEntity(
                exercise.toEntity(workoutId)
            ).toInt()

            val setEntities = exercise.sets.map {
                it.toEntity(exerciseId)
            }

            workoutDao.insertSetEntity(setEntities)
        }
    }


}