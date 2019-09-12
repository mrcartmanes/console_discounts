package com.consolediscounts.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.consolediscounts.R
import com.consolediscounts.domain.interfaces.IStore
import com.consolediscounts.entities.Discount
import com.consolediscounts.entities.Platform
import com.consolediscounts.fragments.DiscountsFragment
import kotlinx.android.synthetic.main.tab.view.*

class ViewPagerAdapter(
    fm: FragmentManager,
    private var context: Context,
    private var storesAndPlatforms: List<Pair<IStore, Platform>>
) : FragmentStatePagerAdapter(fm) {

    var discountsFilter: String = ""
        set(value) {
            field = value
            fragments.forEach { it.value.discountsFilter = value }
            tabViews.forEach { (i, view) -> view.textView.text = storeHeader(i) }
        }

    private val fragments: MutableMap<Int, DiscountsFragment> = mutableMapOf()
    private val tabViews: MutableMap<Int, View> = mutableMapOf()
    private val discounts: MutableMap<Int, MutableSet<Discount>> = mutableMapOf()

    override fun getItem(position: Int) = DiscountsFragment()
    override fun getCount() = storesAndPlatforms.size
    override fun getPageTitle(position: Int): String = ""

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as DiscountsFragment
        fragment.discountsFilter = discountsFilter
        fragments[position] = fragment
        discounts[position]?.forEach { fragment.showDiscount(it) }
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        fragments.remove(position)
    }

    fun addDiscount(discount: Discount) {
        val index = storesAndPlatforms.indexOfFirst { it.first.name == discount.store.name && it.second == discount.platform }
        if (index >= 0) {
            discounts[index]?.add(discount) ?: discounts.put(index, mutableSetOf(discount))
            fragments[index]?.showDiscount(discount)
            tabViews[index]?.textView?.text = storeHeader(index)
        }
    }

    fun getTabView(position: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.tab, null)
        view.tagView.text = storesAndPlatforms[position].second.platformShortName
        view.tagView.color = Color.parseColor(storesAndPlatforms[position].second.color)
        view.textView.text = storeHeader(position)
        tabViews[position] = view
        return view
    }

    private fun storeHeader(position: Int): String {
        val store = storesAndPlatforms[position].first.name
        val count = discounts[position]?.count { discountsFilter.isEmpty() || it.game.contains(discountsFilter, true) } ?: 0
        return "$store [$count]"
    }
}