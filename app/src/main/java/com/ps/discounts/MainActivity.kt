package com.ps.discounts

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.psdiscounts.entities.Discount
import com.psdiscounts.kodein
import com.psdiscounts.presentation.DiscountsPresenter
import com.psdiscounts.presentation.IDiscountsView
import org.kodein.di.erased.instance

class MainActivity : AppCompatActivity(), IDiscountsView {

    private val discountsPresenter: DiscountsPresenter by kodein.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        Log.i("[${discount.store}]", discount.toString())
    }

    override fun discountsFinished() {
        Log.i("[DISCOUNTS]", "DONE!")
    }
}
