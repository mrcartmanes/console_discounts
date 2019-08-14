package com.psdiscounts.presentation

import com.psdiscounts.domain.GetDiscounts
import com.psdiscounts.domain.threadContext
import com.psdiscounts.domain.uiContext
import com.psdiscounts.entities.Discount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface IDiscountsView {
    fun showDiscount(discount: Discount)
    fun discountsFinished()
}

class DiscountsPresenter(private val getDiscountsUseCase: GetDiscounts) : BasePresenter<IDiscountsView>(threadContext) {

    @ExperimentalCoroutinesApi
    override fun onViewAttached(view: IDiscountsView) {
        presenterScope.launch {
            getDiscountsUseCase(
                onSuccess = { discounts ->
                    launch {
                        for (discount in discounts) {
                            withContext(uiContext) { view.showDiscount(discount) }
                        }
                        withContext(uiContext) { view.discountsFinished() }
                    }
                },
                onFailure = { /* FIXME Logger.e(TAG, "Could not get discounts", it) */ }
            )
        }
    }
}
