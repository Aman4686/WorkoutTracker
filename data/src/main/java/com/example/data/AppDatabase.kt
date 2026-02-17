package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.api.WorkoutDao
import com.example.data.model.ExerciseEntity
import com.example.data.model.SetEntity
import com.example.data.model.WorkoutEntity

@Database(entities = [WorkoutEntity::class, ExerciseEntity::class, SetEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}
