package com.jichicristina_moldovanpaul.bad_habits_tracker.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "habits",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["userId"])
    ]
)
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val habitName: String,
    val startDate: Long,
    val userId: Int
)
