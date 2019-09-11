package com.consolediscounts

import com.consolediscounts.data.stores.EShop
import com.consolediscounts.data.stores.GoodsRu
import com.consolediscounts.data.stores.Microsoft
import com.consolediscounts.data.stores.PSN
import com.consolediscounts.domain.GetDiscounts
import com.consolediscounts.domain.interfaces.IStore
import com.consolediscounts.presentation.DiscountsPresenter
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

expect val kodeinPlatformModule: Kodein.Module

val kodein = Kodein {
    import(kodeinPlatformModule)
    bind<IStore>(tag = "goods.ru") with singleton { GoodsRu(instance(), instance()) }
    bind<IStore>(tag = "psn") with singleton { PSN(instance(), instance()) }
    bind<IStore>(tag = "eshop") with singleton { EShop(instance()) }
    bind<IStore>(tag = "microsoft") with singleton { Microsoft(instance(), instance()) }
    bind<DiscountsPresenter>() with singleton { DiscountsPresenter(instance()) }
    bind<GetDiscounts>() with singleton { GetDiscounts() }
}