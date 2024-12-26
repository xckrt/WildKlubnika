package com.example.wildklubnika.editproduct
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Dao
import com.example.wildklubnika.AppDatabase
import com.example.wildklubnika.R
import com.example.wildklubnika.Tovars
import com.example.wildklubnika.databinding.FragmentEditProductBinding
import com.example.wildklubnika.dataclasses.Product
import com.example.wildklubnika.interfaces.CategoryDao
import kotlinx.coroutines.launch
import java.io.File
import kotlin.properties.Delegates

class EditProductFragment : Fragment() {
    private val pickImageRequestCode = 101
    private val takePhotoRequestCode = 102
    private lateinit var binding: FragmentEditProductBinding
    private lateinit var product:Product
    private lateinit var categoryDao: CategoryDao
    private lateinit var categorySpinner: Spinner
    private var selectedCategory:String = ""
    var selectedCategoryId by Delegates.notNull<Int>()
    lateinit var categoryNames:List<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProductBinding.inflate(inflater, container, false)
        arguments?.let {
            product = it.getParcelable("product") ?: throw IllegalArgumentException("Product not found")
        }
        categorySpinner = binding.productCategorySpinner
        // Извлекаем переданный объект Product из Bundle
        val product = arguments?.getParcelable<Product>("product")
        categoryNames = emptyList()
        lifecycleScope.launch {
            // Загружаем категории из базы данных
            categoryDao = AppDatabase.getDatabase(requireContext()).categoryDao()
            val categories = categoryDao.getAllCategories()
            categoryNames = categories.map { it.name }
            val button = binding.changeImageButton.setOnClickListener{
                showImageSourceDialog()
            }
            // Устанавливаем адаптер для Spinner
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter

            // Устанавливаем слушатель для выбора категории
            categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Устанавливаем выбранную категорию
                    selectedCategory = categoryNames[position]

                    // Получаем ID категории
                    lifecycleScope.launch {
                        Log.d("category", "Selected category: $selectedCategory")
                        val categoryId = categoryDao.getCategoryIdByName(selectedCategory)
                        if (categoryId != null) {
                            selectedCategoryId = categoryId
                            Log.d("category", "Category ID: $selectedCategoryId")
                        } else {
                            Log.d("category", "Category not found for: $selectedCategory")
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Ничего не выбрано
                    Log.d("category", "No category selected")
                }
            }
        }

        // Проверка, если продукт существует, заполняем поля
        product?.let { product ->

            binding.productName.setText(product.name)
            binding.productDescription.setText(product.description)
            binding.productPrice.setText(product.price.toString())
            binding.productQuantity.setText(product.quantity.toString())
            binding.productImage
            lifecycleScope.launch {
                Log.d("category","$selectedCategory")
//                val categoryId = categoryDao.getCategoryIdByName(selectedCategory)
//                selectedCategoryId = categoryId
            }



            // Преобразуем значение quantity в нужный формат (например, Enum)
            val stockStatus = when {
                product.quantity > 10 -> "Many"
                product.quantity in 1..10 -> "Few"
                else -> "Out of Stock"
            }
            binding.productQuantityStatus.text = stockStatus
        }

        // Устанавливаем слушатели для кнопок редактирования
        binding.saveButton.setOnClickListener {
            val updatedProduct = product?.let { it1 ->
                Product(
                    id = it1.id,
                    name = binding.productName.text.toString(),
                    categoryId = selectedCategoryId,
                    description = binding.productDescription.text.toString(),
                    price = binding.productPrice.text.toString().toDouble(),
                    quantity = binding.productQuantity.text.toString().toInt(),
                    sellerId = product.sellerId,
                    image = product.image, // Путь к сохранённому изображению
                    rating = binding.productRating.text.toString().toDouble()
                )
            }
            if (updatedProduct != null) {
                updateProduct(updatedProduct)
            }
        }


        binding.deleteButton.setOnClickListener {
            // Удаление продукта
            deleteProduct(product)
        }

        return binding.root
    }
    private fun showImageSourceDialog() {
        val options = arrayOf("Выбрать из галереи", "Сделать фото")
        AlertDialog.Builder(requireContext())
            .setTitle("Выберите источник изображения")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> pickImageFromGallery()
                    1 -> takePhotoWithCamera()
                }
            }
            .show()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, pickImageRequestCode)
    }

    private fun takePhotoWithCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, takePhotoRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                pickImageRequestCode -> {
                    val uri = data?.data
                    uri?.let {
                        val imagePath = saveImageFromUri(it)
                        product.image = imagePath // Сохраняем путь в объекте Product
                        binding.productImage.setImageURI(uri)
                    }
                }
                takePhotoRequestCode -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        val imagePath = saveImageFromBitmap(it)
                        product.image = imagePath // Сохраняем путь в объекте Product
                        binding.productImage.setImageBitmap(it)
                    }
                }
            }
        }
    }

    private fun saveImageFromBitmap(bitmap: Bitmap): String {
        val fileName = "product_${System.currentTimeMillis()}.jpg" // Уникальное имя файла
        val file = File(requireContext().filesDir, fileName)

        file.outputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return file.absolutePath // Возвращаем путь для сохранения в базе данных
    }

    private fun saveImageFromUri(uri: Uri): String {
        val fileName = "product_${System.currentTimeMillis()}.jpg" // Уникальное имя файла
        val file = File(requireContext().filesDir, fileName)

        requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file.absolutePath // Возвращаем путь для сохранения в базе данных
    }

    private fun updateProduct(product: Product) {
        lifecycleScope.launch {
            val productDao = AppDatabase.getDatabase(requireContext()).productDao()
            productDao.updateProduct(product)
            findNavController().popBackStack()
        }
    }

    private fun deleteProduct(product: Product?) {
        lifecycleScope.launch {
            product?.let {
                val productDao = AppDatabase.getDatabase(requireContext()).productDao()
                productDao.deleteProduct(it)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }
}
