package com.psdiscounts.presentation

import com.psdiscounts.domain.GetDiscounts
import com.psdiscounts.domain.threadContext
import com.psdiscounts.domain.uiContext
import com.psdiscounts.entities.Discount
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface IDiscountsView {
    fun showDiscount(discount: Discount)
    fun discountsFinished()
}

class DiscountsPresenter(private val getDiscountsUseCase: GetDiscounts) : BasePresenter<IDiscountsView>(threadContext) {

    private val discounts: MutableList<Discount> = mutableListOf()
    private var discountsJob: Job = Job()

    init {
        discountsJob.cancel()
    }

    fun getDiscounts() {
        if (discountsJob.isCompleted) {
            discounts.clear()
            discountsJob = presenterScope.launch {
                getDiscountsUseCase(
                    onSuccess = { discountsChannel ->
                        discountsJob = launch {
                            for (discount in discountsChannel) {
                                withContext(uiContext) { view?.showDiscount(discount) }
                                discounts.add(discount)
                            }
                            withContext(uiContext) { view?.discountsFinished() }
                        }
                    },
                    onFailure = { /* FIXME Logger.e(TAG, "Could not get discounts", it) */ }
                )
            }
        }
    }

    override fun onViewAttached(view: IDiscountsView) {
        discounts.forEach { view.showDiscount(it) }
    }
}
