package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "exerciseTypes", indices = [Index(value = ["name"], unique = true)])
data class ExerciseTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val exerciseTypeId: Int = 0,
    val name: String
)
