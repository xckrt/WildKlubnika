package com.example.wildklubnika.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.wildklubnika.dataclasses.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category):Long

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    // Получить все категории
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>
    @Query("SELECT name from categories where id =:id")
    suspend fun getCategoryById(id:Int):String
    @Query("SELECT id from categories where name =:name")
    suspend fun getCategoryIdByName(name:String):Int
    @Query("SELECT COUNT(*) > 0 FROM categories WHERE name = :name")
    suspend fun isCategoryExists(name: String): Boolean
}