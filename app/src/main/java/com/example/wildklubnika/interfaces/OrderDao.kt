package com.example.wildklubnika.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wildklubnika.dataclasses.Order

@Dao
interface OrderDao {

    @Insert
    suspend fun insertOrder(order: Order)

    @Query("SELECT * FROM orders WHERE sellerId = :sellerId")
    fun getOrdersBySeller(sellerId: Int): LiveData<List<Order>>
}