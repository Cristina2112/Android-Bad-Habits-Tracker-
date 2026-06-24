package com.jichicristina_moldovanpaul.bad_habits_tracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUser(username: String, password: String): UserEntity?

    @Query("SELECT COUNT(*) FROM users WHERE LOWER(username) = LOWER(:username)")
    suspend fun checkUserExists(username: String): Int

    @Query("SELECT motivationMsg FROM users WHERE id = :userId")
    fun getUserMotivation(userId: Int): kotlinx.coroutines.flow.Flow<String>

    @Query("SELECT motivationMsg FROM users WHERE id = :userId")
    suspend fun getUserMotivationSuspend(userId: Int): String?
}
