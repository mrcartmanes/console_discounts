package com.consolediscounts.domain.interfaces

import com.consolediscounts.entities.Discount
import com.consolediscounts.entities.Platform

interface IStore {
    val supportedPlatforms: List<Platform>
    val name: String
    val currency: String

    fun getDiscounts(platform: Platform): Sequence<Discount>
}