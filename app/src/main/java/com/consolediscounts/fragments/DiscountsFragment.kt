package com.consolediscounts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.consolediscounts.R
import com.consolediscounts.adapters.DiscountsViewAdapter
import com.consolediscounts.entities.Discount
import kotlinx.android.synthetic.main.fragment_discounts.view.*

class DiscountsFragment : Fragment() {

    var discountsFilter: String = ""
        set(value) {
            (view?.discountsView?.adapter as DiscountsViewAdapter?)?.discountsFilter = value
            field = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_discounts, container, false)
        v.discountsView.adapter = DiscountsViewAdapter()
        v.discountsView.layoutManager = LinearLayoutManager(container?.context)
        return v
    }

    fun showDiscount(discount: Discount) {
        (view?.discountsView?.adapter as? DiscountsViewAdapter)?.showDiscount(discount)
    }
}