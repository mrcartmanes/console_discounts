package com.psdiscounts.domain.interfaces

import com.psdiscounts.entities.Discount
import com.psdiscounts.entities.Platform

interface IStore {
    val supportedPlatforms: List<Platform>
    val name: String

    fun getDiscounts(platform: Platform): Sequence<Discount>
}