package com.ctis487.marketsim.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ctis487.marketsim.model.Product
import com.ctis487.marketsim.R
import com.ctis487.marketsim.db.CartViewModel


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
        val layout = R.layout.recycle_product
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = recyclerItemValues[position]
        holder as ItemViewHolder

        holder.tvName.text = product.name
        holder.tvPrice.text = "$${product.price}"

        holder.btnDetail.setOnClickListener {
            recyclerAdapterInterface.displayItem(product)
        }

        holder.btnCart.setOnClickListener {
            cartViewModel?.addToCart(product) ?: recyclerAdapterInterface.addCart(product)
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