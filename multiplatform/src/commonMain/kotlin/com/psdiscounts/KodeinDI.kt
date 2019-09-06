package com.psdiscounts

import com.psdiscounts.data.stores.GoodsRu
import com.psdiscounts.data.stores.PSN
import com.psdiscounts.domain.GetDiscounts
import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.presentation.DiscountsPresenter
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

expect val kodeinPlatformModule: Kodein.Module

val kodein = Kodein {
    import(kodeinPlatformModule)
    bind<IStore>(tag = "goods.ru") with singleton { GoodsRu(instance(), instance()) }
    bind<IStore>(tag = "psn") with singleton { PSN(instance(), instance()) }
    bind<List<IStore>>() with singleton { listOf<IStore>(instance("psn"), instance("goods.ru")) }
    bind<DiscountsPresenter>() with singleton { DiscountsPresenter(instance()) }
    bind<GetDiscounts>() with singleton { GetDiscounts() }
}