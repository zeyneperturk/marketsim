package com.ctis487.marketsim.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ctis487.marketsim.R
import com.ctis487.marketsim.model.Coupon

class CouponAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var recyclerItemValues = emptyList<Coupon>()

    fun setData(items: List<Coupon>) {
        recyclerItemValues = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = R.layout.item_coupon
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("CouponAdapter", "onBindViewHolder called for position $position")

        val item = recyclerItemValues[position]
        holder as ItemViewHolder

        Log.d("CouponAdapter", "Binding coupon: ${item.code}")

        holder.tvCode.text = item.code
        holder.tvDiscount.text = "${item.discount}%"
    }

    override fun getItemCount(): Int {
        return recyclerItemValues.size
    }

    inner class ItemViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {
        val tvCode: TextView = itemView.findViewById(R.id.tvCode)
        val tvDiscount: TextView = itemView.findViewById(R.id.tvDiscount)
    }
}
