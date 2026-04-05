package com.example.data.repository

import androidx.room.Transaction
import com.example.data.api.WorkoutDao
import com.example.data.model.mappers.mapToDomain
import com.example.data.model.mappers.toEntity
import com.example.domain.di.IoDispatcher
import com.example.domain.model.Exersice
import com.example.domain.model.Workout
import com.example.domain.repository.WorkoutRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : WorkoutRepository {

    override fun getWorkoutsFlow(): Flow<List<Workout>> {
        return workoutDao.getWorkoutsFlow()
            .map { workoutWithExersicesList ->
                workoutWithExersicesList.map { it.mapToDomain() }
            }
    }

    override suspend fun deleteWorkout(id: Int) {
        workoutDao.deleteWorkout(id)
    }

    override suspend fun getWorkouts(): List<Workout> {
        return workoutDao.getWorkouts()
            .map { workoutWithExersicesList ->
                workoutWithExersicesList.mapToDomain()
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

        workout.exersiceList.forEach { exersice ->

            val exersiceId = workoutDao.insertExersiceEntity(
                exersice.toEntity(workoutId)
            ).toInt()

            val setEntities = exersice.sets.map {
                it.toEntity(exersiceId)
            }

            workoutDao.insertSetEntity(setEntities)
        }
    }

    @Transaction
    override suspend fun putWorkout(workout: Workout) {
        val workoutId = workout.id
        workoutDao.upsertWorkoutEntity(workout.toEntity())
        workoutDao.deleteExersicesByWorkoutId(workoutId)

        workout.exersiceList.forEach { exersice ->
            val exersiceId = workoutDao.insertExersiceEntity(
                exersice.toEntity(workoutId)
            ).toInt()

            workoutDao.insertSetEntity(
                exersice.sets.map { it.toEntity(exersiceId) }
            )
        }
    }


}