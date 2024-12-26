package com.example.wildklubnika.dataclasses
enum class StockStatus(val displayName: String) {
    MANY("Много"),
    FEW("Мало"),
    OUT_OF_STOCK("Закончился");

    companion object {
        fun fromQuantity(quantity: Int): StockStatus {
            return when {
                quantity > 10 -> MANY
                quantity in 1..10 -> FEW
                else -> OUT_OF_STOCK
            }
        }
    }
}