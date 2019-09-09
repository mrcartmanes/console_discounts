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
            notifyDataSetChanged()
        }

    private val stores: List<IStore> by kodein.instance()
    private val fragments: MutableMap<Int, DiscountsFragment> = mutableMapOf()
    private val discounts: MutableMap<Int, MutableSet<Discount>> = mutableMapOf()

    override fun getItem(position: Int) = DiscountsFragment()
    override fun getCount() = stores.flatMap { it.supportedPlatforms }.size

    override fun getPageTitle(position: Int): String {
        val storesAndPlatforms =
            stores.flatMap { store -> store.supportedPlatforms.map { store to it } }
        val store = storesAndPlatforms[position].first.name
        val platform = storesAndPlatforms[position].second.platformShortName
        val count = discounts[position]?.count { it.game.contains(discountsFilter, true) } ?: 0
        return "$store [$count] $platform"
    }

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
        val storesAndPlatforms =
            stores.flatMap { store -> store.supportedPlatforms.map { store to it } }
        val index =
            storesAndPlatforms.indexOfFirst { it.first.name == discount.store && it.second == discount.platform }
        if (index >= 0) {
            discounts[index]?.add(discount) ?: discounts.put(index, mutableSetOf(discount))
            fragments[index]?.showDiscount(discount)
            notifyDataSetChanged()
        }
    }
}