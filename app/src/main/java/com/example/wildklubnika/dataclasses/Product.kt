package com.example.wildklubnika.dataclasses

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date


@Entity(tableName = "products")
@Parcelize
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "categoryId")
    val categoryId:Int,
    @ColumnInfo(name = "description")
    val description: String, // Исправлено имя поля для приведения к единому стилю
    @ColumnInfo(name = "image")
    var image: String, // Для Room лучше хранить путь или URL изображения
    @ColumnInfo(name = "price")
    val price: Double, // Используем Double для учета цен с копейками
    @ColumnInfo(name = "rating")
    val rating: Double,
    @ColumnInfo(name = "quantity")
    var quantity: Int,
    @ColumnInfo(name = "sellerId") // Добавлено имя колонки для четкого соответствия базе
    val sellerId: Int
):Parcelable

