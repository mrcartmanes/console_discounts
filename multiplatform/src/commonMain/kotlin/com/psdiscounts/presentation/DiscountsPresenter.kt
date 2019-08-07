package com.psdiscounts.presentation

import com.psdiscounts.domain.GetDiscounts
import com.psdiscounts.domain.threadContext
import com.psdiscounts.entities.Discount
import kotlinx.coroutines.launch
import mu.KotlinLogging

interface IDiscountsView {
    fun showDiscount(discount: Discount)
}

class DiscountsPresenter(private val getDiscountsUseCase: GetDiscounts) : BasePresenter<IDiscountsView>(threadContext) {

    private val logger = KotlinLogging.logger {}

    override fun onViewAttached(view: IDiscountsView) {
        presenterScope.launch {
            getDiscountsUseCase(
                onSuccess = { discounts -> discounts.forEach { logger.info { it } } },
                onFailure = { logger.error(it) {} }
            )
        }
    }
}
