package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.domain.model.Set

@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseOwnerId")]
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0,

    val exerciseOwnerId: Int,

    val weight: String,
    val reps: String
)