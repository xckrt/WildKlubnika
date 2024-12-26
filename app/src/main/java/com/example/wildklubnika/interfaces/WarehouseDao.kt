package com.example.wildklubnika.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wildklubnika.dataclasses.Sklad

@Dao
interface WarehouseDao {

    @Insert
    suspend fun insertWarehouse(warehouse: Sklad)

    @Query("SELECT * FROM sklads")
    fun getAllWarehouses(): LiveData<List<Sklad>>
    @Query("UPDATE sklads SET isEmpty = :isEmpty WHERE id = :id")
    fun isntEmpty(id: Int, isEmpty: Boolean)

}
