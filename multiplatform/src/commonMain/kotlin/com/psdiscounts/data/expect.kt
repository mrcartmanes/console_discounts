package com.psdiscounts.data

import com.psdiscounts.data.stores.MVideo
import com.psdiscounts.domain.GetDiscounts
import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.presentation.DiscountsPresenter
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

expect fun isDigit(c: Char): Boolean
expect val kodeinPlatformModule: Kodein.Module

val kodein = Kodein {
    import(kodeinPlatformModule)
    bind<IStore>(tag = "mvideo") with singleton { MVideo(instance(), instance()) }
    bind<DiscountsPresenter>() with singleton { DiscountsPresenter(instance()) }
    bind<GetDiscounts>() with singleton { GetDiscounts(listOf(instance("mvideo"))) }
}