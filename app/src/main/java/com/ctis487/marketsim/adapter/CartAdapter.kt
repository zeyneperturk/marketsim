package com.ctis487.marketsim.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ctis487.marketsim.R
import com.ctis487.marketsim.databinding.RecyclerCartBinding
import com.ctis487.marketsim.db.CartViewModel
import com.ctis487.marketsim.model.CartItem
import com.ctis487.marketsim.model.Product
import com.ctis487.marketsim.util.Constants
import com.bumptech.glide.Glide

class CartAdapter(
    private val context: Context,
    private val cartViewModel: CartViewModel? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var recyclerItemValues = emptyList<CartItem>()

    fun setData(items: List<CartItem>) {
        recyclerItemValues = items
        notifyDataSetChanged()
    }

    interface RecyclerAdapterInterface {
        fun removeFromCart(pid: Int)
        fun displayItem(product: Product)
    }

    lateinit var recyclerAdapterInterface: RecyclerAdapterInterface

    init {
        recyclerAdapterInterface = context as RecyclerAdapterInterface
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerCartBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return CartItemViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(myRecyclerViewItemHolder: RecyclerView.ViewHolder, position: Int) {
        Log.d("CartAdapter", "onBindViewHolder called for position $position")

        val currentItem = recyclerItemValues[position]
        var itemHolder: CartItemViewHolder = myRecyclerViewItemHolder as CartItemViewHolder

        Log.d("CartAdapter", "Binding cart item: ${currentItem.product.name}")

        itemHolder.binding.tvGtItemProductName.text = currentItem.product.name
        itemHolder.binding.tvGtItemProductPrice.text = "$${currentItem.product.price}"

        val imgUrlAddress = Constants.baseUrlForImage + currentItem.product.img
        Log.d("CartAdapter", "IMG URL: $imgUrlAddress")

        /*
        Glide is a popular image loading and caching library for Android applications written in Kotlin.
        Glide is an open-source library that simplifies the process of loading images from the internet,
        local storage, or other sources into your Android app
         */
        Glide.with(context)
            .load(imgUrlAddress)
            .override(400)
            .into(itemHolder.binding.imgProduct)


        if(position % 2 == 0)
            itemHolder.binding.itemGtLayout.setBackgroundColor(Color.rgb(208, 225, 232))
        else
            itemHolder.binding.itemGtLayout.setBackgroundColor(Color.rgb(235, 223, 225))

        val quantities = mutableListOf<String>()
        for (i in 1..10) {
            quantities.add(i.toString())
        }
        val spinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, quantities)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemHolder.binding.spinner.adapter = spinnerAdapter

        var currentQuantity = currentItem.cart.quantity
        if (currentQuantity < 1) {
            currentQuantity = 1
        }
        if (currentQuantity > 10) {
            currentQuantity = 10
        }
        itemHolder.binding.spinner.setSelection(currentQuantity - 1)

        itemHolder.binding.spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val newQuantity = quantities[pos].toInt()
                if (newQuantity != currentItem.cart.quantity) {
                    if (cartViewModel != null) {
                        cartViewModel.setQuantity(currentItem.product, newQuantity)
                    }
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        itemHolder.binding.imgProduct.setOnClickListener {
            recyclerAdapterInterface.displayItem(currentItem.product)
        }

        itemHolder.binding.btnRemove.setOnClickListener {
            if (cartViewModel != null) {
                if (currentItem.cart.quantity > 1) {
                    cartViewModel.decreaseQuantity(currentItem.product, currentItem.cart.quantity)
                } else {
                    cartViewModel.removeFromCart(currentItem.cart.pid) { numberOfDeleted ->
                        if (numberOfDeleted > 0) {
                            recyclerAdapterInterface.removeFromCart(currentItem.cart.pid)
                        }
                    }
                }
            } else {
                recyclerAdapterInterface.removeFromCart(currentItem.cart.pid)
            }
        }
    }

    override fun getItemCount(): Int {
        return recyclerItemValues.size
    }

    inner class CartItemViewHolder(var binding: RecyclerCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
