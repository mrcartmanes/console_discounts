package com.ps.discounts.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ps.discounts.R
import com.psdiscounts.entities.Discount
import kotlinx.android.synthetic.main.discount_card.view.*

class DiscountsViewAdapter : RecyclerView.Adapter<DiscountsViewAdapter.ViewHolder>() {

    val discounts: MutableList<Discount> = mutableListOf()

    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun setDiscount(discount: Discount) {
            discount.poster?.let { posterBytes ->
                v.poster.setImageBitmap(BitmapFactory.decodeByteArray(posterBytes, 0, posterBytes.size))
            }
            v.oldPrice.text = discount.oldPrice.toString()
            v.currentPrice.text = discount.newPrice.toString()
            v.gameName.text = discount.game
        }
    }

    override fun getItemCount(): Int = discounts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDiscount(discounts[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.discount_card, parent, false))
    }

    fun showDiscount(discount: Discount) {
        discounts.add(discount)
        notifyDataSetChanged()
    }
}