package com.example.wildklubnika.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finances")
data class Finance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sellerId: Int,          // ID продавца, для которого ведутся финансы
    val income: Double,         // Доходы продавца
    val expenses: Double,       // Расходы продавца
    val profit: Double,         // Прибыль продавца (можно вычислить: income - expenses)
    val month: String,          // Месяц/Год для отчетности (например: "2024-11")
    val year: Int,              // Год отчетности
    val balance: Double         // Текущий баланс (с учетом всех операций)
)