package com.example.data.api

import androidx.room.Dao
import androidx.room.Query
import com.example.data.model.Workout

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout")
    suspend fun getWorkouts(): List<Workout>
}
