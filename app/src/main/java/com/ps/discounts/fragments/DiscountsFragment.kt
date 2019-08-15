package com.ps.discounts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ps.discounts.R
import com.ps.discounts.adapters.DiscountsViewAdapter
import com.psdiscounts.entities.Discount
import kotlinx.android.synthetic.main.fragment_discounts.*
import kotlinx.android.synthetic.main.fragment_discounts.view.*

class DiscountsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_discounts, container, false)
        view.discountsView.adapter = DiscountsViewAdapter()
        view.discountsView.layoutManager = LinearLayoutManager(container?.context)
        return view
    }

    fun addDiscount(discount: Discount) {
        (discountsView.adapter as DiscountsViewAdapter).addDiscount(discount)
    }
}