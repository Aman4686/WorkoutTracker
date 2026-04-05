package com.example.data.di

import com.example.data.repository.ExerciseTypeRepositoryImpl
import com.example.data.repository.WorkoutRepositoryImpl
import com.example.domain.WorkoutDomain
import com.example.domain.WorkoutDomainImpl
import com.example.domain.repository.ExerciseTypeRepository
import com.example.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(
        workoutRepositoryImpl: WorkoutRepositoryImpl
    ): WorkoutRepository

    @Binds
    @Singleton
    abstract fun bindExerciseTypeRepository(
        exerciseTypeRepositoryImpl: ExerciseTypeRepositoryImpl
    ): ExerciseTypeRepository


}