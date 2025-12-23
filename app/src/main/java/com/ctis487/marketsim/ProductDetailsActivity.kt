package com.ctis487.marketsim

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.ctis487.lab.myapplication.LocaleHelper
import com.ctis487.marketsim.databinding.ActivityProductDetailsBinding
import com.ctis487.marketsim.db.CartViewModel
import com.ctis487.marketsim.model.Product
import com.ctis487.marketsim.util.Constants
import com.bumptech.glide.Glide

class ProductDetailsActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    lateinit var binding: ActivityProductDetailsBinding
    private lateinit var cartViewModel: CartViewModel
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        product = intent.getParcelableExtra<Product>("product")
        val currentProduct = product
        if (currentProduct != null) {
            binding.tvGtItemProductName.text = currentProduct.name
            binding.tvGtItemProductPrice.text = "$${currentProduct.price}"
            binding.tvGtItemProductDescription.text = currentProduct.description

            val imgUrlAddress = Constants.baseUrlForImage + currentProduct.img

            Glide.with(this)
                .load(imgUrlAddress)
                .override(600)
                .into(binding.imageView)
        }

        binding.addCartBtn.setOnClickListener {
            val currentProduct = product
            if (currentProduct != null) {
                cartViewModel.addToCart(currentProduct)
                Toast.makeText(this, "${currentProduct.name} added to cart", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}