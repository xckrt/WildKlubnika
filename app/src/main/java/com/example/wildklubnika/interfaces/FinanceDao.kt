package com.example.wildklubnika.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wildklubnika.dataclasses.Finance
import com.example.wildklubnika.dataclasses.Transaction

@Dao
interface FinanceDao {

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM finances WHERE sellerId = :sellerId")
    fun getFinancesBySeller(sellerId: Int): LiveData<List<Finance>>
}
