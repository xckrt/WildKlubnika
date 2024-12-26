package com.example.wildklubnika.Warehouses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.R
import com.example.wildklubnika.dataclasses.Sklad

/**
 * A simple [Fragment] subclass.
 * Use the [SelectWarehouseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectWarehouseFragment : Fragment() {

    private lateinit var warehouseRecyclerView: RecyclerView
    private lateinit var warehouseAdapter: WarehouseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_warehouse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        warehouseRecyclerView = view.findViewById(R.id.warehouseRecyclerView)
        warehouseRecyclerView.layoutManager = LinearLayoutManager(context)

        // Загружаем список складов
        loadWarehouses()

        warehouseAdapter = WarehouseAdapter() // Адаптер для складов
        warehouseRecyclerView.adapter = warehouseAdapter
    }

    private fun loadWarehouses() {
        // Пример данных для тестирования
        val warehouseList = listOf(
            Sklad(1, "Москва", "Московский тракт,8","Московский",null,false),
            Sklad(2, "Санкт-Петербург", "Кольцовский пр-кт,4","Санкт-Петербургский",null,true),
            Sklad(3, "Екатеринбург", "Первомайская ул.,73","Екатеринбургский",null,false)
        )

        // Передача данных в адаптер
        warehouseAdapter.setWarehouses(warehouseList)
    }
}