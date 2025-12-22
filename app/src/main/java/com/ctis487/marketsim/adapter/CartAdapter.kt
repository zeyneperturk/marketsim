package com.ctis487.marketsim.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ctis487.marketsim.R
import com.ctis487.marketsim.db.CartViewModel
import com.ctis487.marketsim.model.Cart
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = R.layout.recycler_cart
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("CartAdapter", "onBindViewHolder called for position $position")

        val item = recyclerItemValues[position]
        holder as ItemViewHolder

        Log.d("CartAdapter", "Binding cart item: ${item.product.name}")

        holder.tvName.text = item.product.name
        holder.tvPrice.text = "$${item.product.price}"

        val imgUrlAddress = Constants.baseUrlForImage + item.product.img
        Log.d("CartAdapter", "IMG URL: $imgUrlAddress")

        /*
        Glide is a popular image loading and caching library for Android applications written in Kotlin.
        Glide is an open-source library that simplifies the process of loading images from the internet,
        local storage, or other sources into your Android app
         */
        Glide.with(context)
            .load(imgUrlAddress)
            .override(400)
            .into(holder.imgProduct)

        // Set up quantity spinner
        val quantities = (1..10).map { it.toString() }
        val spinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, quantities)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinnerQuantity.adapter = spinnerAdapter
        
        // Remove listener temporarily to avoid triggering updates when setting selection
        holder.spinnerQuantity.onItemSelectedListener = null
        
        // Set current quantity - ensure it's within valid range
        val currentQuantity = item.cart.quantity.coerceIn(1, 10)
        val currentQuantityIndex = currentQuantity - 1
        if (currentQuantityIndex >= 0 && currentQuantityIndex < quantities.size) {
            holder.spinnerQuantity.setSelection(currentQuantityIndex, false)
        }

        // Update quantity when spinner changes
        holder.spinnerQuantity.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val newQuantity = quantities[pos].toInt()
                // Only update if quantity actually changed - use setQuantity to set exact value
                if (newQuantity != item.cart.quantity) {
                    cartViewModel?.setQuantity(item.product, newQuantity)
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        // Product image click - navigate to product details
        holder.imgProduct.setOnClickListener {
            recyclerAdapterInterface.displayItem(item.product)
        }

        // Remove button click - decrease quantity or remove if quantity is 1
        holder.btnRemove.setOnClickListener {
            if (item.cart.quantity > 1) {
                // Decrease quantity by 1
                cartViewModel?.decreaseQuantity(item.product, item.cart.quantity)
            } else {
                // Remove item completely if quantity is 1
                cartViewModel?.removeFromCart(item.cart.pid) { numberOfDeleted ->
                    if (numberOfDeleted > 0) {
                        recyclerAdapterInterface.removeFromCart(item.cart.pid)
                    }
                } ?: recyclerAdapterInterface.removeFromCart(item.cart.pid)
            }
        }
    }

    override fun getItemCount(): Int {
        return recyclerItemValues.size
    }

    inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemove)
        val tvName: TextView = itemView.findViewById(R.id.tvGtItemProductName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvGtItemProductPrice)
        val spinnerQuantity: Spinner = itemView.findViewById(R.id.spinner)
    }
}
