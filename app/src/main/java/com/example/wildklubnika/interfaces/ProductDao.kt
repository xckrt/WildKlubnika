package com.example.wildklubnika.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.wildklubnika.dataclasses.Product
import com.example.wildklubnika.dataclasses.ProductWithSeller

@Dao
interface ProductDao {
        @Insert
        suspend fun insertProduct(product: Product)

        @Update
         suspend fun updateProduct(product: Product)

        @Delete
         suspend fun deleteProduct(product: Product)

        @Query("SELECT * FROM products")
         fun getAllProducts(): LiveData<List<Product>>

        @Query("SELECT * FROM products WHERE id = :productId")
       fun getProductById(productId: Int): LiveData<Product>

    @Transaction
    @Query("SELECT * FROM products WHERE id = :productId")
     fun getProductWithSellerById(productId: Int): LiveData<ProductWithSeller>
    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    suspend fun getProductsByCategory(categoryId: Int): List<Product>



}