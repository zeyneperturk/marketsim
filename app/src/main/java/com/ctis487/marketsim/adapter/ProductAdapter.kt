package com.ctis487.marketsim.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ctis487.marketsim.util.Constants
import com.ctis487.marketsim.model.Product
import com.ctis487.marketsim.R
import com.ctis487.marketsim.db.CartViewModel
import com.bumptech.glide.Glide

class ProductAdapter(
    private val context: Context,
    private val cartViewModel: CartViewModel? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var recyclerItemValues = emptyList<Product>()

    fun setData(items:List<Product>){
        recyclerItemValues = items
        notifyDataSetChanged()
    }

    interface RecyclerAdapterInterface {
        fun displayItem(product: Product)
        fun addCart(product: Product)
    }

    lateinit var recyclerAdapterInterface: RecyclerAdapterInterface

    init {
        recyclerAdapterInterface = context as RecyclerAdapterInterface
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = R.layout.recycler_product
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("Adapter", "onBindViewHolder called for position $position")

        val item = recyclerItemValues[position]
        holder as ItemViewHolder

        Log.d("Adapter", "Binding product: ${item.name}")

        holder.tvName.text = item.name
        holder.tvPrice.text = "$${item.price}"

        val imgUrlAddress = Constants.baseUrlForImage + item.img
        Log.d("IMG URL", imgUrlAddress)

        /*
        Glide is a popular image loading and caching library for Android applications written in Kotlin.
        Glide is an open-source library that simplifies the process of loading images from the internet,
        local storage, or other sources into your Android app
         */
        Glide.with(context)
            .load(imgUrlAddress)
            .override(400)
            .into(holder.btnDetail)

        holder.btnDetail.setOnClickListener {
            recyclerAdapterInterface.displayItem(item)
        }

        holder.btnCart.setOnClickListener {
            if (cartViewModel != null) {
                cartViewModel.addToCart(item)
                android.widget.Toast.makeText(context, "${item.name} added to cart", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                recyclerAdapterInterface.addCart(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return recyclerItemValues.size
    }

    inner class ItemViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {
        val btnDetail: ImageView = itemView.findViewById(R.id.imgProduct)
        val btnCart: ImageView = itemView.findViewById(R.id.imgCart)
        val tvName: TextView = itemView.findViewById(R.id.tvGtItemProductName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvGtItemProductPrice)
    }

}