package com.example.wildklubnika.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sklads")
data class Sklad(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    var city:String,
    var address:String,
    var name:String,
    var products: List<Product>?,
    var isEmpty:Boolean)