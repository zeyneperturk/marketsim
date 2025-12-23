package com.ctis487.marketsim

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctis487.lab.myapplication.LocaleHelper
import com.ctis487.marketsim.adapter.CartAdapter
import com.ctis487.marketsim.databinding.ActivityCartBinding
import com.ctis487.marketsim.databinding.DialogUsecouponBinding
import com.ctis487.marketsim.db.CartViewModel
import com.ctis487.marketsim.db.CouponDAO
import com.ctis487.marketsim.db.MarketRoomDatabase
import com.ctis487.marketsim.model.Product

class CartActivity : AppCompatActivity(), CartAdapter.RecyclerAdapterInterface {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    lateinit var binding: ActivityCartBinding
    lateinit var adapter: CartAdapter
    private lateinit var cartViewModel: CartViewModel
    private lateinit var couponDAO: CouponDAO
    private var couponsList: List<com.ctis487.marketsim.model.Coupon> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        
        val db = MarketRoomDatabase.getDatabase(this)
        couponDAO = db.CouponDAO()

        adapter = CartAdapter(this, cartViewModel)
        binding.recylerMed.layoutManager = LinearLayoutManager(this)
        binding.recylerMed.adapter = adapter

        binding.buttonBack.setNavigationOnClickListener {
            finish()
        }

        binding.btnCoupon.setOnClickListener {
            showCouponDialog()
        }

        cartViewModel.getCartItemsWithProducts().observe(this) { cartItems ->
            adapter.setData(cartItems)
        }

        cartViewModel.getOriginalTotal().observe(this) { originalTotal ->
            binding.tvCost.text = String.format("$%.2f", originalTotal)
        }

        cartViewModel.getTotalCost().observe(this) { discountedTotal ->
            binding.tvDiscountTotal.text = String.format("$%.2f", discountedTotal)
        }

        couponDAO.getAllCoupons().observe(this) { coupons ->
            couponsList = coupons
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

    private fun showCouponDialog() {
        val dialogBinding = DialogUsecouponBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        val couponStrings = mutableListOf<String>()
        couponStrings.add("-")
        for (coupon in couponsList) {
            couponStrings.add("${coupon.code} - ${coupon.discount}% off")
        }

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            couponStrings
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinner2.adapter = spinnerAdapter

        val currentCoupon = cartViewModel.getSelectedCoupon()
        if (currentCoupon != null) {
            for (i in couponsList.indices) {
                if (couponsList[i].cid == currentCoupon.cid) {
                    dialogBinding.spinner2.setSelection(i + 1)
                    break
                }
            }
        }

        dialogBinding.button.setOnClickListener {
            val selectedPosition = dialogBinding.spinner2.selectedItemPosition
            if (selectedPosition == 0) {
                cartViewModel.applyCoupon(null)
            } else {
                val couponIndex = selectedPosition - 1
                if (couponIndex < couponsList.size) {
                    val selectedCoupon = couponsList[couponIndex]
                    cartViewModel.applyCoupon(selectedCoupon)
                }
            }
            dialog.dismiss()
        }

        dialog.show()
    }
}