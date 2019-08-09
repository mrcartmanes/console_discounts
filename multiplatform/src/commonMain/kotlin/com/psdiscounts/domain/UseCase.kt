package com.psdiscounts.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

abstract class UseCase<out Type> {

    abstract suspend fun run(scope: CoroutineScope): Type

    suspend operator fun invoke(onSuccess: (Type) -> Unit, onFailure: (Exception) -> Unit) {
        coroutineScope {
            try {
                val result = run(this)
                launch(uiContext) { onSuccess(result) }
            } catch (e: Exception) {
                launch(uiContext) { onFailure(e) }
            }
        }
    }
}