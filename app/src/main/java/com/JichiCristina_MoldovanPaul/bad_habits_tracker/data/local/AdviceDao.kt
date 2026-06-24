package com.jichicristina_moldovanpaul.bad_habits_tracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AdviceDao {
    @Insert
    suspend fun insertAdvice(advice: AdviceEntity): Long

    @Query("SELECT * FROM advices WHERE userId = :userId ORDER BY id DESC LIMIT 1")
    fun getLatestAdvice(userId: Int): Flow<AdviceEntity?>
}
