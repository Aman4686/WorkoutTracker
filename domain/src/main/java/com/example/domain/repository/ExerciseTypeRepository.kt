package com.example.domain.repository

import com.example.domain.model.ExerciseType
import com.example.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseTypeRepository {

    fun getExerciseTypeFlow(): Flow<List<ExerciseType>>

    suspend fun getExerciseTypes(): List<ExerciseType>

    suspend fun addExerciseType(exerciseType: ExerciseType)

}