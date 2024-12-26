package com.example.wildklubnika.addproduct

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.wildklubnika.AppDatabase
import com.example.wildklubnika.dataclasses.Product
import com.example.wildklubnika.interfaces.ProductDao
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productDao: ProductDao = AppDatabase.getDatabase(application).productDao()
    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

    suspend fun addProduct(product: Product) {
        viewModelScope.launch {
            productDao.insertProduct(product)
        }
    }

    suspend fun updateProduct(product: Product) {
        viewModelScope.launch {
            productDao.updateProduct(product)
        }
    }

    suspend fun getProductById(id: Int): LiveData<Product> {
        return productDao.getProductById(id)
    }
}