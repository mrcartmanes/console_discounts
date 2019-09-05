package com.ps.discounts.adapters

import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ps.discounts.R
import com.psdiscounts.entities.Discount
import kotlinx.android.synthetic.main.discount_card.view.*

class DiscountsViewAdapter : RecyclerView.Adapter<DiscountsViewAdapter.ViewHolder>() {

    var discountsFilter: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val discounts: MutableSet<Discount> = mutableSetOf()
    private var filteredDiscounts = discounts.toList()

    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun setDiscount(discount: Discount) {
            discount.poster?.let { posterBytes ->
                v.poster.setImageBitmap(BitmapFactory.decodeByteArray(posterBytes, 0, posterBytes.size))
            }
            v.oldPrice.text = discount.oldPrice.toString()
            v.currentPrice.text = discount.newPrice.toString()
            v.gameName.text = discount.game
            v.viewInBrowser.setOnClickListener {
                try {
                    val browserIntent = Intent(ACTION_VIEW, Uri.parse(discount.url))
                    v.context.startActivity(browserIntent)
                } catch (e: Exception) {
                    v.context.apply {
                        AlertDialog.Builder(v.context)
                            .setTitle(getString(R.string.smth_went_wrong))
                            .setMessage(getString(R.string.could_not_open_url))
                            .setPositiveButton(getString(R.string.ok)) { dialog: DialogInterface?, _ -> dialog?.cancel() }
                            .create()
                            .show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        filteredDiscounts = discounts.filter {
            discountsFilter.isEmpty() || it.game.contains(
                discountsFilter,
                true
            )
        }
        return filteredDiscounts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDiscount(filteredDiscounts[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.discount_card, parent, false)
        view.setOnTouchListener { v, _ ->
            val inputMethodManager =
                parent.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
        return ViewHolder(view)
    }

    fun showDiscount(discount: Discount) {
        discounts.add(discount)
        notifyDataSetChanged()
    }
}