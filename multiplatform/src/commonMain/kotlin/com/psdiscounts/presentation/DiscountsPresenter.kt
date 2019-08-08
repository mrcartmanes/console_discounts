package com.psdiscounts.presentation

import com.psdiscounts.domain.GetDiscounts
import com.psdiscounts.domain.threadContext
import com.psdiscounts.entities.Discount
import kotlinx.coroutines.launch

interface IDiscountsView {
    fun showDiscount(discount: Discount)
}

class DiscountsPresenter(private val getDiscountsUseCase: GetDiscounts) : BasePresenter<IDiscountsView>(threadContext) {

    override fun onViewAttached(view: IDiscountsView) {
        presenterScope.launch {
            getDiscountsUseCase(
                onSuccess = { discounts ->
                    presenterScope.launch {
                        discounts.forEach { view.showDiscount(it) }
                    }
                },
                onFailure = { /* FIXME Logger.e(TAG, "Could not get discounts", it) */ }
            )
        }
    }
}
