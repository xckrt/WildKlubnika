package com.example.wildklubnika.trackorders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.R
import com.example.wildklubnika.adapters.OrdersAdapter
import com.example.wildklubnika.dataclasses.Order

class TrackOrdersFragment : Fragment() {

    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter
    private val orders: List<Order> = listOf() // Замените на загрузку данных из базы

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_track_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView)
        ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Установка адаптера
        ordersAdapter = OrdersAdapter(orders) { order ->
            // Действие при клике на заказ
            Toast.makeText(context, "Clicked Order ID: ${order.id}", Toast.LENGTH_SHORT).show()
        }
        ordersRecyclerView.adapter = ordersAdapter
    }
}
