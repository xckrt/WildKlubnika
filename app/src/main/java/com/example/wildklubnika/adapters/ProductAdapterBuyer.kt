package com.example.wildklubnika.adapters

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.ProductDiffCallback
import com.example.wildklubnika.R
import com.example.wildklubnika.dataclasses.Product
import java.io.File

class ProductAdapterBuyer(
    val productList: MutableList<Product> = mutableListOf(),
    private val onProductClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapterBuyer.ProductBuyerViewHolder>() {

    class ProductBuyerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.buyer_product_image)
        val productName: TextView = itemView.findViewById(R.id.buyer_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.buyer_product_price)
        val productRating: RatingBar = itemView.findViewById(R.id.buyer_product_rating)
        val productRatingValue: TextView = itemView.findViewById(R.id.buyer_product_rating_value)
        val addToCartButton: Button = itemView.findViewById(R.id.buyer_add_to_cart_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBuyerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductBuyerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductBuyerViewHolder, position: Int) {
        val product = productList[position]
        val file = File(product.image)
        if (file.exists()) {
            holder.productImage.setImageURI(Uri.fromFile(file))
        } else {
            holder.productImage.setImageResource(R.drawable.ic_placeholder) // Заглушка, если файл не найден
        }

        // Установка данных

        holder.productName.text = product.name
        holder.productPrice.text = "${product.price} ₽"
        holder.productRating.rating = product.rating.toFloat()
        holder.productRatingValue.text = product.rating.toString()

        // Обработка кликов
        holder.itemView.setOnClickListener { onProductClick(product) }
        holder.addToCartButton.setOnClickListener { onAddToCartClick(product) }
    }
    fun setProducts(newProducts: List<Product>) {
        val diffCallback = ProductDiffCallback(productList, newProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        productList.clear()
        productList.addAll(newProducts)
        diffResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int = productList.size
}
