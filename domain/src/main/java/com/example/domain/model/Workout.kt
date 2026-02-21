package com.example.domain.model

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
    }
}