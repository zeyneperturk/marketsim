package com.ctis487.marketsim

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctis487.lab.myapplication.LocaleHelper
import com.ctis487.marketsim.adapter.ProductAdapter
import com.ctis487.marketsim.databinding.ActivitySearchBinding
import com.ctis487.marketsim.model.Product

class SearchActivity : AppCompatActivity(),
    ProductAdapter.RecyclerAdapterInterface {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase))
        )
    }

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: ProductAdapter
    private var productList: ArrayList<Product> = arrayListOf()
    private var filteredList: ArrayList<Product> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup back button
        binding.buttonBack.setNavigationOnClickListener {
            finish()
        }

        productList =
            intent.getParcelableArrayListExtra("productList") ?: arrayListOf()

        adapter = ProductAdapter(this)
        binding.recyclerSearch.layoutManager = LinearLayoutManager(this)
        binding.recyclerSearch.adapter = adapter

        binding.searchProduct.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val query = s.toString().trim()

                filteredList = if (query.isEmpty()) {
                    arrayListOf()
                } else {
                    ArrayList(
                        productList.filter {
                            it.name.contains(query, ignoreCase = true)
                        }
                    )
                }

                adapter.setData(filteredList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchProduct.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val query = binding.searchProduct.text.toString().trim()

                val exactMatch = productList.find {
                    it.name.equals(query, ignoreCase = true)
                }

                if (exactMatch != null) {
                    val intent = Intent(this, ProductDetailsActivity::class.java)
                    intent.putExtra("product", exactMatch)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
                }
                true
            } else false
        }
    }

    override fun displayItem(product: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }

    override fun addCart(product: Product) {

    }
}


