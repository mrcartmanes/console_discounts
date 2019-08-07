package com.psdiscounts.domain.interfaces

import com.psdiscounts.entities.Discount

interface IStore {
    fun getDiscounts(): Sequence<Discount>
}