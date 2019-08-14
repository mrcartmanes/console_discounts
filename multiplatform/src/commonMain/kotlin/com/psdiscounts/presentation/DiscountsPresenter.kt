package com.psdiscounts.presentation

import com.psdiscounts.domain.GetDiscounts
import com.psdiscounts.domain.threadContext
import com.psdiscounts.domain.uiContext
import com.psdiscounts.entities.Discount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

interface IDiscountsView {
    fun showDiscount(discount: Discount)
    fun discountsUpdateFinished()
}

class DiscountsPresenter(private val getDiscountsUseCase: GetDiscounts) : BasePresenter<IDiscountsView>(threadContext) {

    @ExperimentalCoroutinesApi
    override fun onViewAttached(view: IDiscountsView) {
        presenterScope.launch {
            getDiscountsUseCase(
                onSuccess = { discounts ->
                    launch {
                        while (true) {
                            val discount = select<Discount?> { discounts.onReceiveOrNull { discount -> discount } }
                            launch(uiContext) {
                                discount?.let { view.showDiscount(it) } ?: view.discountsUpdateFinished()
                            }
                            if (discount == null) break
                        }
                    }
                },
                onFailure = { /* FIXME Logger.e(TAG, "Could not get discounts", it) */ }
            )
        }
    }
}
