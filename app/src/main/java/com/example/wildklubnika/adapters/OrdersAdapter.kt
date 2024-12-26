package com.example.wildklubnika.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.R
import com.example.wildklubnika.dataclasses.Order

class OrdersAdapter(
    private val orders: List<Order>,
    private val onItemClick: (Order) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
        holder.itemView.setOnClickListener { onItemClick(order) }
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderIdTextView: TextView = itemView.findViewById(R.id.orderIdTextView)
        private val orderStatusTextView: TextView = itemView.findViewById(R.id.orderStatusTextView)
        private val orderDateTextView: TextView = itemView.findViewById(R.id.orderDateTextView)
        private val orderTotalPriceTextView: TextView = itemView.findViewById(R.id.orderTotalPriceTextView)

        fun bind(order: Order) {
            orderIdTextView.text = "Order ID: #${order.id}"
            orderStatusTextView.text = "Status: ${order.status.name}"
            orderDateTextView.text = "Date: ${order.orderDate}"
            orderTotalPriceTextView.text = "Total Price: $${order.totalPrice}"
        }
    }
}
