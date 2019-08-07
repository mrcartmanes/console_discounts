package com.ps.discounts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.psdiscounts.data.HtmlParser
import com.psdiscounts.data.URLDownload
import com.psdiscounts.data.interfaces.IHtmlParser
import com.psdiscounts.data.interfaces.IURLDownload
import com.psdiscounts.data.stores.MVideo
import com.psdiscounts.domain.GetDiscounts
import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.entities.Discount
import com.psdiscounts.presentation.DiscountsPresenter
import com.psdiscounts.presentation.IDiscountsView
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val kodein = Kodein {
    bind<IURLDownload>() with singleton { URLDownload() }
    bind<IHtmlParser>() with singleton { HtmlParser() }
    bind<IStore>(tag = "mvideo") with singleton { MVideo(instance(), instance()) }
    bind<DiscountsPresenter>() with singleton { DiscountsPresenter(instance()) }
    bind<GetDiscounts>() with singleton { GetDiscounts(listOf(instance("mvideo"))) }
}

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
