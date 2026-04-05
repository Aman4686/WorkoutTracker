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
            entity = ExersiceEntity::class,
            parentColumns = ["exersiceId"],
            childColumns = ["exersiceOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exersiceOwnerId")]
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0,

    val exersiceOwnerId: Int,

    val count: Int,
    val weight: String,
    val reps: String
)