package com.jichicristina_moldovanpaul.bad_habits_tracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val habitName: String,
    val startDate: Long
)
