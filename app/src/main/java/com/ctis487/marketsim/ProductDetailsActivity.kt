package com.ctis487.marketsim

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ctis487.lab.myapplication.LocaleHelper
import com.ctis487.marketsim.databinding.ActivityProductDetailsBinding
import com.ctis487.marketsim.model.Product
import com.ctis487.marketsim.util.Constants
import com.bumptech.glide.Glide

class ProductDetailsActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    lateinit var binding: ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val product = intent.getParcelableExtra<Product>("product")
        if (product != null) {

            binding.tvGtItemProductName.text = product.name
            binding.tvGtItemProductPrice.text = product.price.toString()
            binding.tvGtItemProductDescription.text = product.description

            val imgUrlAddress = Constants.baseUrlForImage + product.img

            Glide.with(this)
                .load(imgUrlAddress)
                .override(600)
                .into(binding.imageView)
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