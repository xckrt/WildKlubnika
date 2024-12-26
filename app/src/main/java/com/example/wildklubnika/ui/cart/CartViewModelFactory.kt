package com.example.wildklubnika.ui.cart


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wildklubnika.interfaces.CartDao

@Suppress("UNCHECKED_CAST")
class CartViewModelFactory(private val cartDao: CartDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(cartDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}