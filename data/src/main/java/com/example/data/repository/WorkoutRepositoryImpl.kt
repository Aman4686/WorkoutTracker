package com.example.data.repository

import android.util.Log
import androidx.room.Transaction
import com.example.data.api.WorkoutDao
import com.example.data.model.mappers.mapToDomain
import com.example.data.model.mappers.toEntity
import com.example.domain.di.IoDispatcher
import com.example.domain.model.Exercise
import com.example.domain.model.Set
import com.example.domain.model.Workout
import com.example.domain.repository.WorkoutRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : WorkoutRepository {

    override fun getWorkoutsFlow(): Flow<List<Workout>> {
        return workoutDao.getWorkoutsFlow()
            .map { workoutWithExercisesList ->
                workoutWithExercisesList.map { it.mapToDomain() }
            }.flowOn(dispatcher)
    }

    override fun getExercisesFlow(workoutId: Int): Flow<List<Exercise>> {
        return workoutDao.getExercisesFlow(workoutId)
            .map { exerciseList ->
                exerciseList.map { it.mapToDomain() }
            }.flowOn(dispatcher)
    }

    override suspend fun deleteWorkout(id: Int) {
        workoutDao.deleteWorkout(id)
    }

    override suspend fun getWorkouts(): List<Workout> = withContext(dispatcher) {
        workoutDao.getWorkouts()
            .map { workoutWithExercisesList ->
                workoutWithExercisesList.mapToDomain()
            }
    }

    override suspend fun getWorkout(id: Int): Workout? {
        return workoutDao.getWorkout(id = id)?.mapToDomain()
    }


    override suspend fun getExercises(workoutId: Int): List<Exercise> {
        return workoutDao.getExercises(workoutId = workoutId)?.map {
            it.mapToDomain()
        } ?: emptyList()
    }

    @Transaction
    override suspend fun addExercise(workoutId: Int, exerciseList: List<Exercise>) {
        exerciseList.forEach { exercise->
            workoutDao.insertExerciseEntity(exercise.toEntity(workoutId))
        }
    }

    override suspend fun addSet(exerciseId: Int, set: Set) {
        workoutDao.insertSetEntity(set.toEntity(exerciseId))
    }

    override suspend fun updateSet(exerciseId: Int, set: Set) {
        workoutDao.upsertSetEntity(set.toEntity(exerciseId))
    }

    @Transaction
    override suspend fun updateSets(sets: List<Set>) {
        sets.forEach { set ->
            workoutDao.updateSetWeightAndReps(set.id, set.weight, set.reps)
        }
    }

    @Transaction
    override suspend fun addWorkout(workout: Workout) = withContext(dispatcher) {

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

            workoutDao.insertSetListEntity(setEntities)
        }

        workoutId
    }

    @Transaction
    override suspend fun putWorkout(workout: Workout): Int {
        val workoutId = workout.id
        workoutDao.upsertWorkoutEntity(workout.toEntity())
        workoutDao.deleteExercisesByWorkoutId(workoutId)

        workout.exerciseList.forEach { exercise ->
            val exerciseId = workoutDao.insertExerciseEntity(
                exercise.toEntity(workoutId)
            ).toInt()

            workoutDao.insertSetListEntity(
                exercise.sets.map { it.toEntity(exerciseId) }
            )
        }
        return workoutId
    }


}