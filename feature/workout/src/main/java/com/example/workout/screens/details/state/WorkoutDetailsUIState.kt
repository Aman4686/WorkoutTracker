package com.example.workout.screens.details.state

import com.example.domain.model.Exercise
import com.example.domain.model.ExerciseType
import com.example.domain.model.Set
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class WorkoutDetailsUIState(
    val isLoading: Boolean = true,
    val exerciseList: ImmutableList<ExerciseUIModel>
) {

    companion object{
        fun initial() = WorkoutDetailsUIState(isLoading = true, exerciseList = persistentListOf())

        fun preview() : WorkoutDetailsUIState{
            return WorkoutDetailsUIState(
                isLoading = false,
                exerciseList = persistentListOf(ExerciseUIModel.preview(), ExerciseUIModel.preview())
            )
        }
    }
}


data class ExerciseUIModel(
    val id: Int = 0,
    val type: ExerciseType,
    val sets: ImmutableList<SetUIModel> = persistentListOf()
) {
    companion object{

        fun preview(): ExerciseUIModel = ExerciseUIModel(
            type = ExerciseType(
                name = "Bench Press"
            ),
            sets = persistentListOf(SetUIModel.preview(), SetUIModel.preview())
        )
    }
}

data class SetEdit(val exerciseId: Int, val set: SetUIModel)

data class SetUIModel(
    val id: Int = 0,
    val count: Int = 1,
    val weight: String = "0",
    val reps: String = "0",
    val isCheck: Boolean = false
) {
    companion object {
        fun preview(): SetUIModel = SetUIModel(
            id = 0,
            count = 1,
            weight = "55",
            reps = "12"
        )
    }
}

fun SetUIModel.toDomain(): Set = Set(
    id = this.id,
    count = this.count,
    weight = this.weight,
    reps = this.reps
)