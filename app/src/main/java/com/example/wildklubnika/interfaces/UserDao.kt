package com.example.wildklubnika.interfaces

import androidx.room.*
import com.example.wildklubnika.dataclasses.user

@Dao
interface UserDao {

    // Вставка нового пользователя
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: user): Long
    @Query("SELECT id FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getCurrentUserId(): Int
    // Получение пользователя по mail
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): user?
    @Query("UPDATE users SET isLoggedIn = 1 WHERE id = :userId")
    suspend fun setUserLoggedIn(userId: Int)

    @Query("UPDATE users SET isLoggedIn = 0 WHERE id = :userId")
    suspend fun setUserLoggedOut(userId: Int)
    @Query("SELECT * FROM users")
    suspend fun getAllUsers():List<user>
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
     suspend fun isEmailRegistered(email: String): Int

    @Query("UPDATE users SET password = :newPassword WHERE id = :userId")
    suspend fun updatePassword(userId: Int, newPassword: String): Int



    @Query("SELECT isSeller FROM users WHERE email = :email LIMIT 1")
    suspend fun isUserSeller(email: String): Boolean
}
