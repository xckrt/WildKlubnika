package com.example.wildklubnika.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val date: Long, // Unix timestamp
    val description: String,
    val userId: Int // Связь с таблицей пользователей
)
