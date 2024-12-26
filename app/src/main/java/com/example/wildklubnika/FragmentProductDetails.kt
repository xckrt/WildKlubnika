package com.example.wildklubnika

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.wildklubnika.dataclasses.CartItem
import com.example.wildklubnika.dataclasses.Product
import com.example.wildklubnika.interfaces.CartDao
import com.example.wildklubnika.interfaces.CategoryDao
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.w3c.dom.Text


class ProductDetailsFragment : Fragment() {


    private lateinit var cartDao:CartDao
    private lateinit var product: Product
    private var categorytext:String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            product = it.getParcelable("product") ?: throw IllegalArgumentException("Product not found")
        }
        // Получаем объект Product из аргументов
        product = arguments?.getParcelable<Product>("product") ?: return
        cartDao = AppDatabase.getDatabase(requireContext()).cartDao()
        // Инициализируем элементы UI
        val productImage: ImageView = view.findViewById(R.id.product_image)
        val productName: TextView = view.findViewById(R.id.product_name)
        val productDescription: TextView = view.findViewById(R.id.product_description)
        var buyerId:Int = 0
        val productQuantity: TextView = view.findViewById(R.id.product_quantity)
        val category:TextView = view.findViewById(R.id.category)
        val buyButton: Button = view.findViewById(R.id.buy_button)

        // Устанавливаем данные товара в элементы UI
        productName.text = product.name
        productDescription.text = product.description
        lifecycleScope.launch { categorytext = AppDatabase.getDatabase(requireContext()).categoryDao().getCategoryById(product.categoryId) }
        category.text = "Категория: ${categorytext}"
        productQuantity.text = "Количество: ${product.quantity}"


        // Загрузка изображения товара с устройства
        if (product.image.isNotEmpty()) {
            Picasso.get()
                .load("file://${product.image}")  // Загружаем изображение из локального пути
                .into(productImage)
        } else {
            // Если изображения нет, показываем изображение по умолчанию
            productImage.setImageResource(R.drawable.ic_placeholder)
        }

        buyButton.setOnClickListener{
            lifecycleScope.launch { buyerId = AppDatabase.getDatabase(requireContext()).userDao().getCurrentUserId() }

            val cartItem = CartItem(
                product = product,
                buyerId = buyerId, // Идентификатор покупателя
                orderquantity = 1, // По умолчанию добавляется 1 товар
                totalPrice = product.price
            )

            lifecycleScope.launch { AppDatabase.getDatabase(requireContext()).cartDao().insertCartItem(cartItem) }



            findNavController().popBackStack()
        }
    }
}

