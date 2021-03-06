package com.consolediscounts.domain

import com.consolediscounts.domain.interfaces.IStore
import com.consolediscounts.entities.Discount
import com.consolediscounts.entities.Platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class GetDiscounts : UseCase<Map<IStore, List<Platform>>, ReceiveChannel<Discount>>() {

    @ExperimentalCoroutinesApi
    override suspend fun run(scope: CoroutineScope, params: Map<IStore, List<Platform>>): ReceiveChannel<Discount> = scope.produce {
        params.flatMap { (store, platforms) -> platforms.map { store.getDiscounts(it) } }
            .map { sequence ->
            launch {
                sequence.forEach { discount -> send(discount) }
            }
        }.joinAll()
    }
}