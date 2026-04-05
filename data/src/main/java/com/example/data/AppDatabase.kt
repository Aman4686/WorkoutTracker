package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.api.ExerciseTypesDao
import com.example.data.api.WorkoutDao
import com.example.data.model.ExerciseTypeEntity
import com.example.data.model.ExersiceEntity
import com.example.data.model.SetEntity
import com.example.data.model.WorkoutEntity

@Database(
    entities = [WorkoutEntity::class, ExersiceEntity::class, SetEntity::class, ExerciseTypeEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseTypesDao(): ExerciseTypesDao
}
