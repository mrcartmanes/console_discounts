package com.consolediscounts.adapters

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.consolediscounts.domain.interfaces.IStore
import com.consolediscounts.entities.Discount
import com.consolediscounts.fragments.DiscountsFragment
import com.consolediscounts.kodein
import org.kodein.di.erased.instance

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var discountsFilter: String = ""
        set(value) {
            field = value
            fragments.forEach { it.value.discountsFilter = value }
        }

    private val stores: List<IStore> by kodein.instance()
    private val fragments: MutableMap<Int, DiscountsFragment> = mutableMapOf()
    private val discounts: MutableMap<Int, MutableSet<Discount>> = mutableMapOf()

    override fun getItem(position: Int) = DiscountsFragment()
    override fun getCount() = stores.size
    override fun getPageTitle(position: Int) =
        "${stores[position].name} [${discounts[position]?.count {
            discountsFilter.isEmpty() || it.game.contains(
                discountsFilter,
                true
            )
        } ?: 0}]"

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
        discounts[index]?.add(discount) ?: discounts.put(index, mutableSetOf(discount))
        fragments[index]?.showDiscount(discount)
        notifyDataSetChanged()
    }
}