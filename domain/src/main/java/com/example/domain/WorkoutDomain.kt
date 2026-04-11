package com.example.domain

import com.example.domain.di.IoDispatcher
import com.example.domain.model.ExerciseType
import com.example.domain.model.Exercise
import com.example.domain.model.Workout
import com.example.domain.repository.ExerciseTypeRepository
import com.example.domain.repository.WorkoutRepository
import jakarta.inject.Inject
import com.example.domain.model.Set
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface WorkoutDomain {
    fun getWorkoutsFlow(): Flow<List<Workout>>

    fun getExerciseTypeFlow(): Flow<List<ExerciseType>>

    fun getExerciseFlow(workoutId: Int): Flow<List<Exercise>>

    suspend fun getExerciseTypes(): List<ExerciseType>

    suspend fun addExerciseType(exerciseType: ExerciseType)

    suspend fun getWorkouts(): List<Workout>

    suspend fun getWorkout(id: Int): Workout?

    suspend fun getExercises(workoutId: Int) : List<Exercise>

    suspend fun deleteWorkout(id: Int)

    suspend fun insertOrUpdateWorkout(workout: Workout): Int

    suspend fun addExercise(workoutId: Int, exerciseList: List<Exercise>)

    suspend fun addSet(exerciseId: Int, set: Set)

    suspend fun updateSet(exerciseId: Int, set: Set)

    suspend fun updateSets(set: List<Set>)

}

class WorkoutDomainImpl @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseTypeRepository: ExerciseTypeRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : WorkoutDomain {

    override fun getWorkoutsFlow(): Flow<List<Workout>> {
        return workoutRepository.getWorkoutsFlow()
    }

    override fun getExerciseTypeFlow(): Flow<List<ExerciseType>> {
        return exerciseTypeRepository.getExerciseTypeFlow()
    }

    override fun getExerciseFlow(workoutId: Int): Flow<List<Exercise>> {
        return workoutRepository.getExercisesFlow(workoutId)
    }

    override suspend fun getExerciseTypes(): List<ExerciseType> {
        return exerciseTypeRepository.getExerciseTypes()
    }

    override suspend fun addExerciseType(exerciseType: ExerciseType) {
        return exerciseTypeRepository.addExerciseType(exerciseType)
    }

    override suspend fun getWorkouts(): List<Workout> {
        return workoutRepository.getWorkouts()
    }

    override suspend fun getWorkout(id: Int): Workout? {
        return workoutRepository.getWorkout(id)
    }

    override suspend fun getExercises(workoutId: Int): List<Exercise> {
        return workoutRepository.getExercises(workoutId)
    }

    override suspend fun deleteWorkout(id: Int) {
        return workoutRepository.deleteWorkout(id)
    }

    override suspend fun insertOrUpdateWorkout(workout: Workout): Int {
        return if (workout.id == 0)
            workoutRepository.addWorkout(workout)
        else
            workoutRepository.putWorkout(workout)
    }

    override suspend fun addExercise(workoutId: Int, exerciseList: List<Exercise>) {
        workoutRepository.addExercise(workoutId = workoutId, exerciseList = exerciseList)
    }

    override suspend fun addSet(exerciseId: Int, set: Set) {
        workoutRepository.addSet(exerciseId = exerciseId, set = set)
    }

    override suspend fun updateSet(exerciseId: Int, set: Set) {
        workoutRepository.updateSet(exerciseId = exerciseId, set = set)
    }

    override suspend fun updateSets(set: List<Set>) {
        workoutRepository.updateSets(set)
    }


}