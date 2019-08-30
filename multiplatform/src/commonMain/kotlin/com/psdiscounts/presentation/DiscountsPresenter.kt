package com.psdiscounts.presentation

import com.psdiscounts.domain.GetDiscounts
import com.psdiscounts.domain.threadContext
import com.psdiscounts.domain.uiContext
import com.psdiscounts.entities.Discount
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

interface IDiscountsView {
    fun showDiscount(discount: Discount)
    fun discountsFinished()
}

class DiscountsPresenter(private val getDiscountsUseCase: GetDiscounts) : BasePresenter<IDiscountsView>(threadContext) {

    private val discounts: MutableList<Discount> = mutableListOf()
    private var discountsJob: Job = Job()
    private var mutex: Mutex = Mutex()

    init {
        discountsJob.cancel()
    }

    fun getDiscounts(force: Boolean = false): Boolean {
        if (discountsJob.isCompleted && (force || discounts.isEmpty())) {
            discounts.clear()
            discountsJob = presenterScope.launch {
                getDiscountsUseCase(
                    onSuccess = { discountsChannel ->
                        discountsJob = launch {
                            for (discount in discountsChannel) {
                                withContext(uiContext) { view?.showDiscount(discount) }
                                mutex.withLock {
                                    discounts.add(discount)
                                }
                            }
                            withContext(uiContext) { view?.discountsFinished() }
                        }
                    },
                    onFailure = {
                        launch(uiContext) { view?.discountsFinished() }
                    }
                )
            }
            return true
        }
        return false
    }

    override fun onViewAttached(view: IDiscountsView) {
        presenterScope.launch {
            mutex.withLock {
                withContext(uiContext) {
                    discounts.forEach { view.showDiscount(it) }
                }
            }
        }
    }
}
