package com.example.workout.screens.details.state

import com.example.domain.model.Exersice

data class WorkoutDetailsUIState(
    val isLoading: Boolean = true,
    val date: String = "",
    val name: String = "",
    val exersiceList: List<Exersice>
) {

    companion object{
        fun initial() = WorkoutDetailsUIState(isLoading = true, exersiceList = emptyList())

        fun preview() : WorkoutDetailsUIState{
            return WorkoutDetailsUIState(
                isLoading = false,
                exersiceList = listOf(Exersice.preview(), Exersice.preview())
            )
        }
    }
}