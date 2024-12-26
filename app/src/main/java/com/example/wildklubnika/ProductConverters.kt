package com.example.wildklubnika

import androidx.room.TypeConverter
import com.example.wildklubnika.dataclasses.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromProductList(products: MutableList<Product>): String {
        return gson.toJson(products)
    }

    @TypeConverter
    fun toProductList(productsJson: String): MutableList<Product> {
        val type = object : TypeToken<MutableList<Product>>() {}.type
        return gson.fromJson(productsJson, type)
    }
}