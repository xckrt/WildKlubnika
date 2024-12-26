package com.example.wildklubnika.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.wildklubnika.dataclasses.CartItem

@Dao
interface CartDao {
    @Insert
    suspend fun insertCartItem(cartItem: CartItem)
    // Получаем все товары в корзине для покупателя
    @Query("SELECT * FROM cart WHERE buyerId = :buyerId")
    suspend fun getCartItems(buyerId: Int): List<CartItem>

    @Query("SELECT SUM(price * quantity) FROM cart WHERE buyerId = :buyerId")
     fun getTotalPrice(buyerId: Int): LiveData<Double>

    @Query("DELETE FROM cart WHERE buyerId = :buyerId")
    suspend fun clearCart(buyerId: Int)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)
    @Delete
    fun deleteCartItem(cartItem: CartItem)
}
