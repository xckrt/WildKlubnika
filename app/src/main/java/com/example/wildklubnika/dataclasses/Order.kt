package com.example.wildklubnika.dataclasses
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sellerId: Int,         // ID продавца, которому принадлежит заказ
    val buyerId: Int,          // ID покупателя
    val products: List<Product>, // Список продуктов, которые были заказаны
    val totalPrice: Double,    // Общая стоимость заказа
    val orderDate: Date = Date(), // Дата создания заказа
    val deliveryDate: Date?,    // Дата доставки (если уже доставлено)
    val status: OrderStatus)    // Статус заказа (например: "Ожидает", "Доставлен", "Отменен")

