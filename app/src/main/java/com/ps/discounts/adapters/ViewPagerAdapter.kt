package com.ps.discounts.adapters

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ps.discounts.fragments.DiscountsFragment
import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.entities.Discount
import com.psdiscounts.kodein
import org.kodein.di.erased.instance

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val stores: List<IStore> by kodein.instance()
    private val fragments: MutableMap<Int, DiscountsFragment> = mutableMapOf()
    private val discounts: MutableMap<Int, MutableList<Discount>> = mutableMapOf()

    override fun getItem(position: Int) = DiscountsFragment()
    override fun getCount() = stores.size
    override fun getPageTitle(position: Int) = "${stores[position].name} (${discounts[position]?.count() ?: 0})"

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)
        fragments[position] = fragment as DiscountsFragment
        discounts[position]?.forEach { fragment.showDiscount(it) }
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        fragments.remove(position)
    }

    fun addDiscount(index: Int, discount: Discount) {
        discounts[index]?.add(discount) ?: discounts.put(index, mutableListOf(discount))
        fragments[index]?.showDiscount(discount)
        notifyDataSetChanged()
    }
}