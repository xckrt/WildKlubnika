package com.example.wildklubnika

import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.adapters.ProductAdapterBuyer
import com.example.wildklubnika.dataclasses.Product
import com.example.wildklubnika.interfaces.ProductDao
import kotlinx.coroutines.launch

class CategoryandProducts: Fragment() {
    private lateinit var productAdapter: ProductAdapterBuyer
    private lateinit var recyclerView: RecyclerView
    private lateinit var productDao: ProductDao

    companion object {
        private const val CATEGORY_ID_KEY = "category_id_key"

        fun newInstance(categoryId: Int): CategoryandProducts {
            val fragment = CategoryandProducts()
            val bundle = Bundle().apply {
                putInt(CATEGORY_ID_KEY, categoryId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val categoryId = arguments?.getInt(CATEGORY_ID_KEY) ?: return
        productDao = AppDatabase.getDatabase(requireContext()).productDao()

        lifecycleScope.launch {
            val products = productDao.getProductsByCategory(categoryId) // Получаем товары по categoryId
            productAdapter = ProductAdapterBuyer(
                products.toMutableList(),
                onProductClick = { product ->
                    openProductDetails(product)
                },
                onAddToCartClick = { product ->
                    // Обработка добавления в корзину
                }
            )

            recyclerView.adapter = productAdapter
        }
    }
    private fun openProductDetails(product: Product) {
        val bundle = Bundle()
        bundle.putParcelable("product",product)
        findNavController().navigate(R.id.categories_to_full,bundle)
    }
}