package com.example.wildklubnika.ui.dashboard
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wildklubnika.adapters.CategoryAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.AppDatabase
import com.example.wildklubnika.CategoryandProducts
import com.example.wildklubnika.R
import com.example.wildklubnika.adapters.ProductAdapterBuyer
import com.example.wildklubnika.databinding.FragmentDashboardBinding
import com.example.wildklubnika.dataclasses.Category
import com.example.wildklubnika.dataclasses.Product
import com.example.wildklubnika.interfaces.CategoryDao
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryDao: CategoryDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewCategories)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Получаем категорию из базы данных через DAO
        categoryDao = AppDatabase.getDatabase(requireContext()).categoryDao()

        lifecycleScope.launch {
            val categories = categoryDao.getAllCategories() // Получаем все категории из базы данных
            categoryAdapter = CategoryAdapter(categories) { category ->
                // При нажатии на категорию, переходим на фрагмент с товарами этой категории
                val productsFragment = CategoryandProducts.newInstance(category.id)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, productsFragment)
                    .addToBackStack(null)
                    .commit()
            }

            recyclerView.adapter = categoryAdapter
        }
    }
}



