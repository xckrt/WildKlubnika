package com.example.wildklubnika.dataclasses
data class Cart(
    val buyerId: Int,
    val products: MutableList<Product> = mutableListOf(),
    var totalCost: Double = 0.0
) {


}
