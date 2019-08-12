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
        val discountsSequences = stores.map { it.getDiscounts() }.toMutableList()
        while (discountsSequences.isNotEmpty()) {
            select<Discount?> {
                discountsSequences.map { sequence ->
                    async {
                        sequence.firstOrNull()?.let { it } ?: discountsSequences.remove(sequence); null
                    }
                }.forEach { discount ->
                        discount.onAwait { value -> value }
                    }
            }?.let { send(it) }
        }
    }
}