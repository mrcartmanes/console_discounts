package com.ps.discounts.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ps.discounts.fragments.DiscountsFragment
import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.entities.Discount
import com.psdiscounts.kodein
import kotlinx.android.synthetic.main.fragment_discounts.*
import org.kodein.di.erased.instance

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val stores: List<IStore> by kodein.instance()
    private val titles: MutableList<String> = stores.map { store -> store.name }.toMutableList()
    private val fragments: List<DiscountsFragment> = stores.map { DiscountsFragment() }

    override fun getItem(position: Int) = fragments[position]
    override fun getCount() = fragments.size
    override fun getPageTitle(position: Int) = titles[position]

    fun addDiscount(index: Int, discount: Discount) {
        fragments[index].addDiscount(discount)
        titles[index] = "${stores[index].name} (${fragments[index].discountsView.adapter?.itemCount ?: 0})"
        notifyDataSetChanged()
    }
}