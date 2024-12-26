package com.example.wildklubnika.ui.cart

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wildklubnika.AppDatabase
import com.example.wildklubnika.R
import com.example.wildklubnika.databinding.FragmentCartBinding
import com.example.wildklubnika.adapters.CartAdapter
import com.example.wildklubnika.dataclasses.CartItem

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var adapter: CartAdapter
    private var buyerId: Int = -1  // Идентификатор покупателя, по умолчанию -1
    private lateinit var binding: FragmentCartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val cartDao = AppDatabase.getDatabase(requireContext()).cartDao()
        cartViewModel = ViewModelProvider(this, CartViewModelFactory(cartDao)).get(CartViewModel::class.java)

        setupRecyclerView(binding)
        loadBuyerId()



        return binding.root
    }

    private fun setupRecyclerView(binding: FragmentCartBinding) {
        adapter = CartAdapter(
            mutableListOf(),
            onQuantityChanged = { cartItem, newQuantity ->
                cartViewModel.updateItemQuantity(cartItem, newQuantity)
            },
            onItemAction = { cartItem, actionType ->
                when (actionType) {
                    CartAdapter.ActionType.DELETE -> deleteCartItem(cartItem)
                    CartAdapter.ActionType.SHARE -> shareCartItem(cartItem)
                }
            }
        )
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter
    }


    private fun loadBuyerId() {
        CoroutineScope(Dispatchers.IO).launch {
            val currentBuyerId = AppDatabase.getDatabase(requireContext()).userDao().getCurrentUserId()
            withContext(Dispatchers.Main) {
                if (currentBuyerId > 0) {
                    buyerId = currentBuyerId
                    loadCart(buyerId)  // Загружаем корзину для текущего покупателя
                }
            }
        }
    }

    private fun loadCart(buyerId: Int) {
        cartViewModel.loadCartItems(buyerId)  // Загружаем товары корзины
        cartViewModel.loadTotalPrice(buyerId)  // Загружаем общую стоимость
        observeViewModel()
    }

    private fun observeViewModel() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items)  // Передаем обновленные товары корзины в адаптер
        }

        cartViewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            binding.totalCostTextView.text = "Общая стоимость: $total ₽"
        }
    }

    private fun deleteCartItem(cartItem: CartItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление товара")
            .setMessage("Вы уверены, что хотите удалить ${cartItem.product.name} из корзины?")
            .setPositiveButton("Да") { _, _ ->
                lifecycleScope.launch {
                    cartViewModel.deleteCartItem(cartItem) // Удаление через ViewModel
                }
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    private fun shareCartItem(cartItem: CartItem) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Посмотрите на этот товар: ${cartItem.product.name}, цена: ${cartItem.totalPrice} ₽")
        }
        startActivity(Intent.createChooser(shareIntent, "Поделиться товаром"))
    }



    private fun shareCart() {
        val cartItems = cartViewModel.cartItems.value.orEmpty()
        if (cartItems.isNotEmpty()) {
            val shareText = cartItems.joinToString("\n") { "${it.product.name} - ${it.orderquantity} шт." }
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Состав вашей корзины:\n$shareText")
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться корзиной"))
        } else {
            Toast.makeText(requireContext(), "Корзина пуста!", Toast.LENGTH_SHORT).show()
        }
    }

}



