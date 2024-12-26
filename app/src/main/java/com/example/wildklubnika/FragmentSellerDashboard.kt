package com.example.wildklubnika
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.wildklubnika.databinding.FragmentSellerDashboardBinding
class FragmentSellerDashboard : Fragment() {
    private lateinit var addProductButton: Button
    private lateinit var ProductsButton: Button
    private lateinit var trackOrdersButton: Button
    private lateinit var selectWarehouseButton: Button
    private lateinit var financesButton: Button
    lateinit var binding:FragmentSellerDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Обработчики для кнопок

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellerDashboardBinding.inflate(inflater, container, false)
        addProductButton = binding.addProductButton
        ProductsButton = binding.ProductsButton
        trackOrdersButton = binding.trackOrdersButton
        selectWarehouseButton = binding.selectWarehouseButton
        financesButton = binding.financesButton
        addProductButton.setOnClickListener { navigateToAddProductFragment() }
        ProductsButton.setOnClickListener { navigateToEditProductFragment() }
        trackOrdersButton.setOnClickListener { navigateToTrackOrdersFragment() }
        selectWarehouseButton.setOnClickListener { navigateToSelectWarehouseFragment() }
        financesButton.setOnClickListener { navigateToFinancesFragment() }
        return binding.root

    }
    private fun navigateToAddProductFragment() {
        findNavController().navigate(R.id.action_LkFragment_to_addProductFragment)
    }

    private fun navigateToEditProductFragment() {
        findNavController().navigate(R.id.action_LkFragment_to_editProductFragment)
    }

    private fun navigateToTrackOrdersFragment() {
        findNavController().navigate(R.id.action_LkFragment_to_trackOrdersFragment)
    }

    private fun navigateToSelectWarehouseFragment() {
        findNavController().navigate(R.id.action_LkFragment_to_selectWarehouseFragment)
    }

    private fun navigateToFinancesFragment() {
        findNavController().navigate(R.id.action_LkFragment_to_financesFragment)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottomNavigationVisibility(true)
    }
}