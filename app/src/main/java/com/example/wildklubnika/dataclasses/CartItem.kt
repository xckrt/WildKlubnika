package com.example.wildklubnika.dataclasses

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val cartid: Int = 0,
    @Embedded
    val product: Product,
    val buyerId:Int,// Связь с классом Product
    var totalPrice: Double,
    var orderquantity: Int
)