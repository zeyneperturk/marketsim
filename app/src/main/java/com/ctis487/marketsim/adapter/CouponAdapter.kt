package com.ctis487.marketsim.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ctis487.marketsim.R
import com.ctis487.marketsim.model.Coupon

class CouponAdapter : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    private var coupons = emptyList<Coupon>()

    inner class CouponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val code: TextView = itemView.findViewById(R.id.tvCode)
        val discount: TextView = itemView.findViewById(R.id.tvDiscount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coupon, parent, false)
        return CouponViewHolder(view)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = coupons[position]
        holder.code.text = coupon.code
        holder.discount.text = "${coupon.discount}%"
    }

    override fun getItemCount() = coupons.size

    fun submitList(list: List<Coupon>) {
        coupons = list
        notifyDataSetChanged()
    }
}
