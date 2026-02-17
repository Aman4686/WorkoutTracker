package com.example.domain.di


import com.example.domain.WorkoutDomain
import com.example.domain.WorkoutDomainImpl
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
    abstract fun bindWorkoutDomain(
        workoutDomainImpl: WorkoutDomainImpl
    ): WorkoutDomain
}