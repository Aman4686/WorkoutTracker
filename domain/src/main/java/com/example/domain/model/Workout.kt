package com.example.domain.model

data class Workout(
    val id: Int = 0,
    val date: String,
    val exersiceList: List<Exersice>
) {
    companion object{
        fun preview() = Workout(
            date = "14.02",
            exersiceList = emptyList()
        )
    }
}