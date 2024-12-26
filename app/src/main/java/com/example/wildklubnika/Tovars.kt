package com.example.wildklubnika

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.adapters.ProductAdapter
import com.example.wildklubnika.addproduct.ProductViewModel
import com.example.wildklubnika.dataclasses.Product
import com.example.wildklubnika.editproduct.EditProductFragment
import com.example.wildklubnika.interfaces.ProductDao

class Tovars : Fragment() {
    private lateinit var warehouseRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productDao: ProductDao
    private lateinit var productViewModel: ProductViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tovars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        // Инициализируем RecyclerView
        warehouseRecyclerView = view.findViewById(R.id.warehouseRecyclerView)
        warehouseRecyclerView.layoutManager = LinearLayoutManager(context)

        // Получаем доступ к DAO
        productDao = AppDatabase.getDatabase(requireContext()).productDao()

        // Инициализируем адаптер (без данных на начальном этапе)
        productAdapter = ProductAdapter { product ->
            // Открываем фрагмент редактирования только по нажатию на элемент
            openEditProductFragment(product)
        }

        // Устанавливаем адаптер для RecyclerView
        warehouseRecyclerView.adapter = productAdapter

        // Загружаем и отображаем товары продавца
        loadProducts()
    }

    private fun loadProducts() {
        // Наблюдаем за LiveData с товарами продавца
        productDao.getAllProducts().observe(viewLifecycleOwner) { products ->
            if (!products.isNullOrEmpty()) {
                displayProducts(products)
            }
        }
    }

    private fun displayProducts(products: List<Product>) {
        // Передаем список продуктов в адаптер
        productAdapter.submitList(products)
    }

    private fun openEditProductFragment(product: Product) {
        // Открываем фрагмент редактирования продукта

        val bundle = Bundle()
        bundle.putParcelable("product",product)

        findNavController().navigate(R.id.edit,bundle)
    }
}

