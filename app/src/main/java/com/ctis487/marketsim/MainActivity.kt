package com.ctis487.marketsim

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctis487.lab.myapplication.LocaleHelper
import com.ctis487.marketsim.databinding.ActivityMainBinding
import com.ctis487.marketsim.model.User
import com.ctis487.marketsim.model.game.GameConstants
import com.ctis487.marketsim.adapter.ProductAdapter
import com.ctis487.marketsim.db.CartDAO
import com.ctis487.marketsim.db.CartViewModel
import com.ctis487.marketsim.db.MarketRoomDatabase
import com.ctis487.marketsim.db.ProductService
import com.ctis487.marketsim.db.ApiClient
import com.ctis487.marketsim.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(),ProductAdapter.RecyclerAdapterInterface {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: ProductAdapter
    private lateinit var cartViewModel: CartViewModel
    lateinit var productService: ProductService
    lateinit var productList: MutableList<Product>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GameConstants.initalization(applicationContext)

        cartViewModel=ViewModelProvider(this).get(CartViewModel::class.java)

        adapter = ProductAdapter(this, cartViewModel)
        binding.recylerMed.layoutManager = LinearLayoutManager(this)
        binding.recylerMed.adapter = adapter

        binding.searchProduct.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {

                binding.searchProduct.performClick()
                val intent = Intent(this, SearchActivity::class.java)
                intent.putParcelableArrayListExtra(
                    "productList",
                    ArrayList(productList)
                )
                startActivity(intent)
            }
            true
        }




        productService = ApiClient.getClient().create(ProductService::class.java)
        var request = productService.getProduct()

        request.enqueue(object : Callback<List<Product>> { //Retrofit will send a request to server
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
            /*
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@MainActivity,
                            "HTTP Error: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }

                    val body = response.body()

                    if (body.isNullOrEmpty()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Product list is empty or null",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }


                    //When response is taken from server that method will be called
                Log.d("API_TEST", "First product = ${response.body()!![0].name}")
                */
                if (response.isSuccessful && response.body() != null) {
                    productList = response.body()!!.toMutableList()
                    adapter.setData(productList)
                }
            }
        })

        binding.buttonCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        binding.buttonProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
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

    override fun addCart(product: Product) {
        cartViewModel.addToCart(product)
        Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
    }
}