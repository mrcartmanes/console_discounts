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
    private val stores: List<IStore> by kodein.instance()
    private val storesAndPlatforms = stores.map { it to it.supportedPlatforms }.toMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

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
                viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
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
}
