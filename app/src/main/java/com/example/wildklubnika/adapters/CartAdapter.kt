package com.example.wildklubnika.adapters
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.R
import com.example.wildklubnika.dataclasses.CartItem
import java.io.File


class CartAdapter(
    private val items: MutableList<CartItem>,
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    private val onItemAction: (CartItem, ActionType) -> Unit // Новый коллбэк для действий
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    enum class ActionType {
        DELETE, SHARE
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val toolbar: ImageButton = itemView.findViewById(R.id.cart_toolbar)
        val quantityControl: TextView = itemView.findViewById(R.id.cart_product_quantity)
        val productName: TextView = itemView.findViewById(R.id.cart_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.cart_product_price)
        val productImage: ImageView = itemView.findViewById(R.id.cart_product_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = items[position]

        // Заполнение данных
        holder.productName.text = cartItem.product.name
        holder.productPrice.text = "${cartItem.totalPrice} ₽"
        holder.quantityControl.text = cartItem.orderquantity.toString()

        // Загрузка изображения из пути, указанного в поле product.image
        val imageFile = File(cartItem.product.image) // product.image должен содержать путь к файлу
        if (imageFile.exists()) {
            holder.productImage.setImageURI(Uri.fromFile(imageFile))
        } else {
            holder.productImage.setImageResource(R.drawable.ic_placeholder) // Плейсхолдер, если файл отсутствует
        }

        // Настройка меню на toolbar
        holder.toolbar.setOnClickListener { view ->
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.cart_toolbar_menu)

            // Обработка действий в меню
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        onItemAction(cartItem, ActionType.DELETE)
                        true
                    }
                    R.id.action_share -> {
                        onItemAction(cartItem, ActionType.SHARE)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }



    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CartItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}



