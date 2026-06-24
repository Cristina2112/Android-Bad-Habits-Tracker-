package com.jichicristina_moldovanpaul.bad_habits_tracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    suspend fun insertHabit(habit: HabitEntity): Long

    @Query("SELECT * FROM habits WHERE userId = :userId ORDER BY id DESC")
    fun getHabitsForUser(userId: Int): Flow<List<HabitEntity>>
}
