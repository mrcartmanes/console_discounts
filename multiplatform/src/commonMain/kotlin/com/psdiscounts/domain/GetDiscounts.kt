package com.psdiscounts.domain

import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.entities.Discount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class GetDiscounts(private val stores: List<IStore>) : UseCase<ReceiveChannel<Discount>>() {

    override suspend fun run(scope: CoroutineScope): ReceiveChannel<Discount> = scope.produce {
        stores.map { it.getDiscounts() }.map { sequence ->
            launch {
                sequence.forEach { discount -> send(discount) }
            }
        }.joinAll()
    }
}