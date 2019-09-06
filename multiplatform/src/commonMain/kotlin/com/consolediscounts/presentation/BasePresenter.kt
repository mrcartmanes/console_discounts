package com.consolediscounts.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<T>(private val coroutineContext: CoroutineContext) {

    protected var view: T? = null
    protected val presenterScope: CoroutineScope = CoroutineScope(coroutineContext)

    fun attachView(view: T) {
        this.view = view
        onViewAttached(view)
    }

    fun detachView() {
        view = null
        onViewDetached()
    }

    fun cancelJobs() {
        presenterScope.cancel()
    }

    protected open fun onViewAttached(view: T) {}
    protected open fun onViewDetached() {}
}
