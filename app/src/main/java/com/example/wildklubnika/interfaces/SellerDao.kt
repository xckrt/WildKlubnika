package com.example.wildklubnika.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.wildklubnika.dataclasses.Seller

@Dao
interface SellerDao {
    @Dao
    interface SellerDao {
        @Query("SELECT * FROM sellers WHERE id = :sellerId")
        fun getSellerById(sellerId: Int): LiveData<Seller>
    }
}