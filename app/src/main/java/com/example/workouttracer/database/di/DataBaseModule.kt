package com.example.workouttracer.database.di

import android.content.Context
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.api.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    @Provides
    fun provideWorkoutDao(
        database: AppDatabase
    ): WorkoutDao {
        return database.workoutDao()
    }
}