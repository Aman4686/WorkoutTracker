package com.example.workout.screens.exersices.state

import com.example.domain.model.Exersice
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ExerciseListUIState(
    val exersiceList: ImmutableList<ExersiceUIModel> = emptyList<ExersiceUIModel>().toImmutableList(),
    val isLoading: Boolean = true,
) {

    companion object{
        fun initial() = ExerciseListUIState(
            exersiceList = listOf(ExersiceUIModel.preview(1), ExersiceUIModel.preview(2)).toImmutableList(),
            isLoading = true
        )

        fun preview() = ExerciseListUIState(
            exersiceList = listOf(ExersiceUIModel.preview(2), ExersiceUIModel.preview(1)).toImmutableList(),
            isLoading = false
        )
    }
}

data class ExersiceUIModel(
    val id: Int = 0,
    val name: String,
    val isSelected: Boolean = false
) {
    companion object{
        fun preview(id: Int) = ExersiceUIModel(id = id,  name = "Bench Press")
    }
}

fun Exersice.toUIModel(): ExersiceUIModel{
    return ExersiceUIModel(id = id, name = type.name)
}