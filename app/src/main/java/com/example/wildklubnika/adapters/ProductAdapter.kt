package com.example.wildklubnika.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.R
import com.example.wildklubnika.dataclasses.Product

class ProductAdapter(
    private val onProductClick: (Product) -> Unit // Обработчик клика по продукту
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.buyer_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.buyer_product_price)

        init {
            // Устанавливаем обработчик клика на весь элемент
            itemView.setOnClickListener {
                // Проверяем валидность позиции
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = getItem(position)
                    onProductClick(product) // Запускаем обработчик
                }
            }
        }

        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = product.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}

// DiffUtil callback для сравнения элементов в списке
class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id // Сравниваем продукты по ID
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem // Сравниваем все поля
    }
}
