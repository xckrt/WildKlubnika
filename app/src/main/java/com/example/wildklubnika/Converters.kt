package com.example.wildklubnika

import androidx.room.TypeConverter
import com.example.wildklubnika.dataclasses.ImageData
import com.example.wildklubnika.dataclasses.OrderStatus
import com.example.wildklubnika.dataclasses.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    @TypeConverter
    fun fromProduct(product: Product?): String? {
        // Преобразуем объект Product в строку (JSON)
        return product?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toProduct(productJson: String?): Product? {
        // Преобразуем строку JSON обратно в объект Product
        return productJson?.let { Gson().fromJson(it, Product::class.java) }
    }
    // Date <-> Long
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // ImageData <-> JSON String
    @TypeConverter
    fun fromJson(value: String?): ImageData? {
        return value?.let {
            val type = object : TypeToken<ImageData>() {}.type
            Gson().fromJson(it, type)
        }
    }

    @TypeConverter
    fun toJson(imageData: ImageData?): String? {
        return Gson().toJson(imageData)
    }

    // List<Product> <-> JSON String
    @TypeConverter
    fun fromProductList(products: List<Product>?): String? {
        return Gson().toJson(products)
    }

    @TypeConverter
    fun toProductList(json: String?): List<Product>? {
        val type = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(json, type)
    }
    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): String {
        return status.name
    }

    @TypeConverter
    fun toOrderStatus(status: String): OrderStatus {
        return OrderStatus.valueOf(status)
    }
}
