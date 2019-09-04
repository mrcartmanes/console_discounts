package com.ps.discounts.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.ps.discounts.R
import com.ps.discounts.adapters.ViewPagerAdapter
import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.entities.Discount
import com.psdiscounts.kodein
import com.psdiscounts.presentation.DiscountsPresenter
import com.psdiscounts.presentation.IDiscountsView
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.erased.instance

class MainActivity : AppCompatActivity(), IDiscountsView {

    private val discountsPresenter: DiscountsPresenter by kodein.instance()
    private val stores: List<IStore> by kodein.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        titleProgressBar.isVisible = discountsPresenter.getDiscounts()

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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                if (discountsPresenter.getDiscounts(true)) {
                    viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
                    titleProgressBar.isVisible = true
                }
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
        val index = stores.indexOfFirst { store -> store.name == discount.store }
        if (index >= 0) {
            (viewPager.adapter as ViewPagerAdapter).addDiscount(index, discount)
            Log.d("[Discounts]", "Add $discount")
        } else {
            Log.w("[Discounts]", "Ignore $discount")
        }
    }

    override fun discountsFinished() {
        titleProgressBar.isVisible = false
    }
}
