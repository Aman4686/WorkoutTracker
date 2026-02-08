package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.api.WorkoutDao
import com.example.data.model.Workout

@Database(entities = [Workout::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}
