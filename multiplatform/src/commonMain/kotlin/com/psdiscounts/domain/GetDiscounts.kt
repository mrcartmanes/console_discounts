package com.psdiscounts.domain

import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.entities.Discount

class GetDiscounts(private val stores: List<IStore>) : UseCase<Sequence<Discount>>() {

    override suspend fun run(): Sequence<Discount> =
        stores.map { store -> store.getDiscounts() }.reduce { seq, seq2 -> seq + seq2 }
}