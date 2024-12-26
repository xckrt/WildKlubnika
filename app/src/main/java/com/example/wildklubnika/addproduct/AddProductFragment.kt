package com.example.wildklubnika.addproduct

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.wildklubnika.AppDatabase
import com.example.wildklubnika.R
import com.example.wildklubnika.dataclasses.Category
import com.example.wildklubnika.dataclasses.ImageData
import com.example.wildklubnika.dataclasses.Product
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

import java.util.Date
import kotlin.properties.Delegates

class AddProductFragment : Fragment() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var edittextCategory:EditText
    private lateinit var productNameEditText: EditText
    private lateinit var productDescriptionEditText: EditText
    private lateinit var productPriceEditText: EditText
    private lateinit var saveButton: Button
    private var ans:Boolean=false
    var categoryId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        edittextCategory = view.findViewById(R.id.productCategoryEditText)
        productNameEditText = view.findViewById(R.id.productNameEditText)
        productDescriptionEditText = view.findViewById(R.id.productDescriptionEditText)
        productPriceEditText = view.findViewById(R.id.productPriceEditText)
        saveButton = view.findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val productName = productNameEditText.text.toString()
            val productDescription = productDescriptionEditText.text.toString()
            val productPrice = productPriceEditText.text.toString().toInt()
            val productCategory = edittextCategory.text.toString()

            lifecycleScope.launch {
                val formattedCategory = productCategory.replaceFirstChar { it.titlecase() }
                // Проверяем, существует ли категория
                val ans = AppDatabase.getDatabase(requireContext()).categoryDao().isCategoryExists(formattedCategory)

                if (!ans) {
                    // Если категория не существует, создаем новую
                    val newCategory = Category(name = formattedCategory)
                    AppDatabase.getDatabase(requireContext()).categoryDao().insertCategory(newCategory)
                }

                // Получаем ID категории
                val categoryId = AppDatabase.getDatabase(requireContext()).categoryDao().getCategoryIdByName(formattedCategory)

                if (categoryId != null) {
                    val newProduct = Product(
                        name = productName,
                        description = productDescription,
                        categoryId = categoryId,
                        image = null.toString(), // Укажите путь к изображению
                        price = productPrice.toDouble(),
                        rating = 0.0,
                        quantity = 1,
                        sellerId = 1
                    )
                    productViewModel.addProduct(newProduct)
                    findNavController().popBackStack()
                } else {
                    Log.d("Категория", "Категория не найдена...")
                }
            }
        }

    }
}
