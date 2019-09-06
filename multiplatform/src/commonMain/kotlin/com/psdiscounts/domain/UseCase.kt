package com.psdiscounts.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

abstract class UseCase<in Params, out Type> {

    abstract suspend fun run(scope: CoroutineScope, params: Params): Type

    suspend operator fun invoke(
        params: Params,
        onSuccess: (Type) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        coroutineScope {
            try {
                val result = run(this, params)
                launch(uiContext) { onSuccess(result) }
            } catch (e: Exception) {
                launch(uiContext) { onFailure(e) }
            }
        }
    }
}