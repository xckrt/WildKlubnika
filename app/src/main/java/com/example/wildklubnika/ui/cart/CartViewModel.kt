package com.example.wildklubnika.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.wildklubnika.dataclasses.CartItem
import com.example.wildklubnika.interfaces.CartDao
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CartViewModel(private val cartDao: CartDao) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice


    fun loadCartItems(buyerId: Int) {
        viewModelScope.launch {
            val items = cartDao.getCartItems(buyerId)  // Загружаем товары корзины для конкретного покупателя
            _cartItems.postValue(items)
        }
    }

    // Метод для загрузки общей стоимости корзины
    fun loadTotalPrice(buyerId: Int) {
        viewModelScope.launch {
            cartDao.getTotalPrice(buyerId).observeForever { totalPrice ->
                _totalPrice.postValue(totalPrice) // Обновляем значение в LiveData
            }
        }
    }

    // Обновление количества товара в корзине
    fun updateItemQuantity(cartItem: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            cartItem.orderquantity = newQuantity
            cartItem.totalPrice = cartItem.totalPrice / cartItem.orderquantity * newQuantity
            cartDao.updateCartItem(cartItem)  // Обновляем корзину в базе
        }
    }
    fun deleteCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            cartDao.deleteCartItem(cartItem) // Удаляем элемент из базы данных

            // Обновляем данные корзины
            val updatedItems = cartDao.getCartItems(cartItem.buyerId)
            _cartItems.postValue(updatedItems)

            // Обновляем общую стоимость
            cartDao.getTotalPrice(cartItem.buyerId).observeForever { totalPrice ->
                _totalPrice.postValue(totalPrice) // Обновляем значение в LiveData
            }

        }
    }



}



