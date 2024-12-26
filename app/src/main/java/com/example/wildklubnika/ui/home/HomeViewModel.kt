package com.example.wildklubnika.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.wildklubnika.AppDatabase
import com.example.wildklubnika.dataclasses.Product
import com.example.wildklubnika.dataclasses.ProductWithSeller
import com.example.wildklubnika.dataclasses.Seller


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val productDao = AppDatabase.getDatabase(getApplication()).productDao()

    val products: LiveData<List<Product>> = productDao.getAllProducts()


    fun getSellerById(sellerId: Int): LiveData<ProductWithSeller> {
        return productDao.getProductWithSellerById(sellerId)
    }
}
