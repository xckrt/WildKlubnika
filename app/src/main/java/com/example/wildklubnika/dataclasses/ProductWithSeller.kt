package com.example.wildklubnika.dataclasses

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithSeller(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "sellerId",
        entityColumn = "id"
    )
    val seller: Seller
)