package com.psdiscounts.domain.interfaces

import com.psdiscounts.entities.Discount

interface IStore {
    val name: String
    fun getDiscounts(): Sequence<Discount>
}