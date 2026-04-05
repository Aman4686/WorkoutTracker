package com.example.domain.repository

import com.example.domain.model.ExerciseType
import com.example.domain.model.Exersice
import kotlinx.coroutines.flow.Flow

interface ExerciseTypeRepository {

    fun getExersiceTypeFlow(): Flow<List<ExerciseType>>


}