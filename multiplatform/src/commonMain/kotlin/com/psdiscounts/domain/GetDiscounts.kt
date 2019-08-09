package com.psdiscounts.domain

import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.entities.Discount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select

class GetDiscounts(private val stores: List<IStore>) : UseCase<ReceiveChannel<Discount>>() {

    @ExperimentalCoroutinesApi
    override suspend fun run(scope: CoroutineScope): ReceiveChannel<Discount> = scope.produce {
        while (true) {
            select<Discount?> {
                stores
                    .map { it.getDiscounts() }
                    .map { async { it.firstOrNull() } }
                    .forEach { discount ->
                        discount.onAwait { value -> value }
                    }
            }?.let { send(it) } ?: break
        }
    }
}