package com.example.workout.screens.exercises.state

import com.example.domain.model.ExerciseType
import com.example.domain.model.Exercise
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ExerciseListUIState(
    val exerciseList: ImmutableList<ExerciseUIModel> = emptyList<ExerciseUIModel>().toImmutableList(),
    val isLoading: Boolean = true,
) {

    companion object{
        fun initial() = ExerciseListUIState(
            exerciseList = emptyList<ExerciseUIModel>().toImmutableList(),
            isLoading = false
        )

        fun preview() = ExerciseListUIState(
            exerciseList = listOf(ExerciseUIModel.preview(2), ExerciseUIModel.preview(1)).toImmutableList(),
            isLoading = false
        )
    }
}

data class ExerciseUIModel(
    val id: Int = 0,
    val name: String,
    val isSelected: Boolean = false
) {
    companion object{
        fun preview(id: Int) = ExerciseUIModel(id = id,  name = "Bench Press")
    }
}

fun ExerciseType.toUIModel(isSelected: Boolean = false): ExerciseUIModel {
    return ExerciseUIModel(id = id, name = name, isSelected = isSelected)
}