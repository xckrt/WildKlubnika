package com.example.wildklubnika.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // ID категории, уникален
    @ColumnInfo(name = "name")
    val name: String   // Название категории
)