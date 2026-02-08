package com.example.domain.useCase

import com.example.domain.di.DefaultDispatcher
import com.example.domain.model.WorkoutDomainModel
import com.example.domain.repository.WorkoutRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface GetWorkoutListUseCase{
    suspend operator fun invoke(): WorkoutDomainModel
}

class GetWorkoutListUseCaseImpl @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : GetWorkoutListUseCase{

    override suspend operator fun invoke(): WorkoutDomainModel{
        return withContext(defaultDispatcher){
            workoutRepository.getWorkoutList()
        }
    }
}