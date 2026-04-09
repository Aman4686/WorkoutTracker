package com.example.workout.screens.exercises.state

import com.example.domain.model.ExerciseType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ExerciseListUIState(
    val exerciseList: ImmutableList<ExerciseTypeUIModel> = persistentListOf(),
    val isLoading: Boolean = true,
) {

    companion object{
        fun initial() = ExerciseListUIState(
            exerciseList = persistentListOf(),
            isLoading = false
        )

        fun preview() = ExerciseListUIState(
            exerciseList = persistentListOf(ExerciseTypeUIModel.preview(2), ExerciseTypeUIModel.preview(1)),
            isLoading = false
        )
    }
}

data class ExerciseTypeUIModel(
    val id: Int = 0,
    val name: String,
    val isSelected: Boolean = false
) {
    companion object{
        fun preview(id: Int) = ExerciseTypeUIModel(id = id,  name = "Bench Press")
    }
}

fun ExerciseType.toUIModel(isSelected: Boolean = false): ExerciseTypeUIModel {
    return ExerciseTypeUIModel(id = id, name = name, isSelected = isSelected)
}

fun ExerciseTypeUIModel.toDomainModel(): ExerciseType {
    return ExerciseType(id = id, name = name)
}