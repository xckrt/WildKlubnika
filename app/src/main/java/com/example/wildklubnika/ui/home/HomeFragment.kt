package com.example.wildklubnika.ui.home
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wildklubnika.AppDatabase
import com.example.wildklubnika.MainActivity
import com.example.wildklubnika.ProductDetailsFragment
import com.example.wildklubnika.R
import com.example.wildklubnika.adapters.ProductAdapterBuyer
import com.example.wildklubnika.dataclasses.CartItem
import com.example.wildklubnika.dataclasses.Product
import com.example.wildklubnika.interfaces.CartDao
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapterBuyer
    var currentUserId by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productDao = AppDatabase.getDatabase(requireContext()).productDao()
        val cartDao = AppDatabase.getDatabase(requireContext()).cartDao()
        val userDao = AppDatabase.getDatabase(requireContext()).userDao()

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val productListLiveData = productDao.getAllProducts()
        lifecycleScope.launch { currentUserId = userDao.getCurrentUserId() }

        productListLiveData.observe(viewLifecycleOwner, Observer { productList ->
            if (productList != null) {
                val mutableProductList = productList.toMutableList()
                adapter = ProductAdapterBuyer(
                    productList = mutableProductList,
                    onAddToCartClick = { product ->
                        addToCart(product, cartDao, currentUserId)
                    },
                    onProductClick = { product ->
                        openProductDetails(product)
                    }
                )
                recyclerView.adapter = adapter
            } else {
                // Обработка ситуации, когда список пустой
                adapter = ProductAdapterBuyer(
                    productList = mutableListOf(),
                    onAddToCartClick = { product -> },
                    onProductClick = { product -> }
                )
                recyclerView.adapter = adapter
            }
        })
    }


    private fun addToCart(product: Product,cartDao: CartDao,currentBuyerId:Int) {
        lifecycleScope.launch { cartDao.insertCartItem(
            CartItem(
                cartid = id,
                product = product,
                buyerId = currentBuyerId,
                orderquantity = 1,
                totalPrice = product.price.toDouble()
            )
        ) }

    }

    private fun openProductDetails(product: Product) {
        val bundle = Bundle()
        bundle.putParcelable("product",product)
        findNavController().navigate(R.id.home_to_full,bundle)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottomNavigationVisibility(true)
    }
}

