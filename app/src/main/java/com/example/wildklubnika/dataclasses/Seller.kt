package com.example.wildklubnika.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sellers")
data class Seller(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val Rating:Double,
    val Products:String,
    val scoreonproducts:Int)
