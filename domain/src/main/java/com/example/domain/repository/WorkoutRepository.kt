package com.example.domain.repository

import com.example.domain.model.WorkoutDomainModel

interface WorkoutRepository {
    suspend fun getWorkoutList(): WorkoutDomainModel
}