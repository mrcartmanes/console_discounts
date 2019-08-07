package com.psdiscounts.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<T>(private val coroutineContext: CoroutineContext) {

    protected var view: T? = null
    protected lateinit var presenterScope: PresenterCoroutineScope

    fun attachView(view: T) {
        this.view = view
        presenterScope = PresenterCoroutineScope(coroutineContext)
        onViewAttached(view)
    }

    fun detachView() {
        view = null
        presenterScope.viewDetached()
        onViewDetached()
    }

    protected open fun onViewAttached(view: T) {}
    protected open fun onViewDetached() {}
}

class PresenterCoroutineScope(context: CoroutineContext) : CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy { context + Job() }

    fun viewDetached() {
        cancel()
    }
}
