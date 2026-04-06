package com.example.domain.model

import utils.DateUtils

data class Workout(
    val id: Int = 0,
    val date: String,
    val exerciseList: List<Exercise>
) {
    companion object{
        fun preview() = Workout(
            date = "14.02",
            exerciseList = emptyList()
        )

        fun new() = Workout(
            date = DateUtils.currentDate(),
            exerciseList = emptyList()
        )
    }
}