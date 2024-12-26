package com.example.wildklubnika.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class user(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // ID пользователя

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "isSeller")
    val isSeller: Boolean,
    @ColumnInfo(name = "isLoggedIn")
    val isLoggedIn: Boolean
)
