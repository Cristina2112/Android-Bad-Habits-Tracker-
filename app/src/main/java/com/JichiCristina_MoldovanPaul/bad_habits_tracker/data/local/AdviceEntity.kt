package com.jichicristina_moldovanpaul.bad_habits_tracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "advices")
data class AdviceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val adviceText: String
)
