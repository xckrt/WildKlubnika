package com.example.wildklubnika.Warehouses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.R
import com.example.wildklubnika.dataclasses.Sklad

class WarehouseAdapter : RecyclerView.Adapter<WarehouseAdapter.WarehouseViewHolder>() {

    private var warehouses: List<Sklad> = listOf()

    // Обновление данных в адаптере
    fun setWarehouses(newWarehouses: List<Sklad>) {
        warehouses = newWarehouses
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_warehouse, parent, false) // Убедитесь, что `item_warehouse` существует
        return WarehouseViewHolder(view)
    }

    override fun onBindViewHolder(holder: WarehouseViewHolder, position: Int) {
        val warehouse = warehouses[position]
        holder.bind(warehouse)
    }

    override fun getItemCount(): Int {
        return warehouses.size
    }

    class WarehouseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val warehouseName: TextView = itemView.findViewById(R.id.warehouseNameTextView)
        private val warehouseStatus: TextView = itemView.findViewById(R.id.warehouseStatusTextView)

        fun bind(warehouse: Sklad) {
            warehouseName.text = warehouse.name
            warehouseStatus.text = if (warehouse.isEmpty) "Пустой" else "Занят"
        }
    }
}

