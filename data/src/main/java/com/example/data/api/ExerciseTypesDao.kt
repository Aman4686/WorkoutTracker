package com.example.data.api

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.model.ExerciseTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseTypesDao {

    @Transaction
    @Query("SELECT * FROM exerciseTypes")
    fun getExerciseTypesFlow(): Flow<List<ExerciseTypeEntity>>

}