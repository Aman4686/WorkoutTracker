package com.example.data.repository

import com.example.data.api.ExerciseTypesDao
import com.example.data.api.WorkoutDao
import com.example.data.model.mappers.mapToDomain
import com.example.domain.di.IoDispatcher
import com.example.domain.model.ExerciseType
import com.example.domain.repository.ExerciseTypeRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ExerciseTypeRepositoryImpl @Inject constructor(
    private val exerciseTypeDao: ExerciseTypesDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ExerciseTypeRepository {

    override fun getExersiceTypeFlow(): Flow<List<ExerciseType>> {
        return exerciseTypeDao.getExerciseTypesFlow().map { it ->
            it.map { it.mapToDomain() }
        }
    }


}