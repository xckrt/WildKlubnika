package com.example.wildklubnika.finances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.wildklubnika.R


class FinancesFragment : Fragment() {

    private lateinit var profitTextView: TextView
    private lateinit var lossTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_finances, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profitTextView = view.findViewById(R.id.profitTextView)
        lossTextView = view.findViewById(R.id.lossTextView)

        // Загружаем финансовые данные
        loadFinancialData()
    }

    private fun loadFinancialData() {
        // Логика для загрузки финансовых данных из базы
    }
}