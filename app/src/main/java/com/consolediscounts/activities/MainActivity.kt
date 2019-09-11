package com.consolediscounts.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.consolediscounts.R
import com.consolediscounts.adapters.ViewPagerAdapter
import com.consolediscounts.domain.interfaces.IStore
import com.consolediscounts.entities.Discount
import com.consolediscounts.kodein
import com.consolediscounts.presentation.DiscountsPresenter
import com.consolediscounts.presentation.IDiscountsView
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.erased.instance


class MainActivity : AppCompatActivity(), IDiscountsView {

    private val discountsPresenter: DiscountsPresenter by kodein.instance()
    private val psn: IStore by kodein.instance("psn")
    private val eShop: IStore by kodein.instance("eshop")
    private val microsoft: IStore by kodein.instance("microsoft")
    private val goodsRu: IStore by kodein.instance("goods.ru")
    private val storesAndPlatforms = listOf(microsoft, eShop, psn, goodsRu).map { it to it.supportedPlatforms }.toMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureViewPager()

        if (discountsPresenter.getDiscounts(storesAndPlatforms)) {
            titleProgressBar.isVisible = true
        }

        setSupportActionBar(toolbar)
        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchMenuItem = menu?.findItem(R.id.search)
        val searchView = searchMenuItem?.actionView as SearchView
        searchMenuItem.setOnActionExpandListener(
            object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                    TransitionManager.beginDelayedTransition(toolbar, Slide())
                    return true
                }

                override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                    TransitionManager.beginDelayedTransition(toolbar, Slide())
                    return true
                }
            }
        )
        searchView.queryHint = getString(R.string.search)
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    (viewPager.adapter as ViewPagerAdapter).discountsFilter = newText ?: ""
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
            }
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                discountsPresenter.refresh(storesAndPlatforms)
                configureViewPager()
                titleProgressBar.isVisible = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        discountsPresenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        discountsPresenter.detachView()
    }

    override fun showDiscount(discount: Discount) {
        (viewPager.adapter as ViewPagerAdapter).addDiscount(discount)
        Log.d("[Discounts]", "Add $discount")
    }

    override fun discountsFinished() {
        titleProgressBar.isVisible = false
    }

    override fun discountsFailed(e: Exception) {
        discountsFinished()
        Log.e("[Discounts]", "Exception occured", e)
    }

    private fun configureViewPager() {
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, this,
            storesAndPlatforms
                .toList()
                .flatMap { (store, platforms) -> platforms.map { platform -> store to platform } }
        )
        tabLayout.setupWithViewPager(viewPager)
        for (tab in 0..tabLayout.tabCount) {
            tabLayout.getTabAt(tab)?.customView = (viewPager.adapter as ViewPagerAdapter).getTabView(tab)
        }
    }
}
