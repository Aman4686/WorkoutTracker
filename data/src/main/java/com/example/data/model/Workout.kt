package com.example.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "payload") val data: String?,
    @ColumnInfo(name = "name") val name: String?
)