package com.example.data.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.model.ExerciseTypeEntity
import com.example.data.model.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseTypesDao {

    @Transaction
    @Query("SELECT * FROM exerciseTypes")
    fun getExerciseTypesFlow(): Flow<List<ExerciseTypeEntity>>

    @Transaction
    @Query("SELECT * FROM exerciseTypes")
    suspend fun getExerciseTypes(): List<ExerciseTypeEntity>

    @Insert
    suspend fun insertExerciseTypeEntity(exercise: ExerciseTypeEntity): Long

}