package com.ps.discounts.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.psdiscounts.entities.Discount

class DiscountsViewAdapter : RecyclerView.Adapter<DiscountsViewAdapter.ViewHolder>() {

    val discounts: MutableList<Discount> = mutableListOf()

    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun setDiscount(discount: Discount) {
            (v as TextView).text = "${discount.game} - ${discount.oldPrice} - ${discount.newPrice}"
        }
    }

    override fun getItemCount(): Int = discounts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDiscount(discounts[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TextView(parent.context))
    }

    fun addDiscount(discount: Discount) {
        discounts.add(discount)
        notifyDataSetChanged()
    }
}