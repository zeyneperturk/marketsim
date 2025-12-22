package com.ctis487.marketsim

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctis487.lab.myapplication.LocaleHelper
import com.ctis487.marketsim.adapter.CartAdapter
import com.ctis487.marketsim.databinding.ActivityCartBinding
import com.ctis487.marketsim.db.CartViewModel
import com.ctis487.marketsim.model.Product

class CartActivity : AppCompatActivity(), CartAdapter.RecyclerAdapterInterface {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    lateinit var binding: ActivityCartBinding
    lateinit var adapter: CartAdapter
    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        adapter = CartAdapter(this, cartViewModel)
        binding.recylerMed.layoutManager = LinearLayoutManager(this)
        binding.recylerMed.adapter = adapter

        binding.buttonBack.setNavigationOnClickListener {
            finish()
        }

        cartViewModel.getCartItemsWithProducts().observe(this) { cartItems ->
            adapter.setData(cartItems)
        }

        cartViewModel.getTotalCost().observe(this) { total ->
            binding.tvCost.text = String.format("$%.2f", total)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun displayItem(product: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }

    override fun removeFromCart(pid: Int) {
    }
}