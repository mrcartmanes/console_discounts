package com.psdiscounts.multiplatform.entities

data class Discount(
    val game: String,
    val posterURL: String,
    val oldPrice: Double,
    val newPrice: Double
) : Comparable<Double> {

    val percent = if (oldPrice == 0.0) 0 else ((oldPrice - newPrice / oldPrice) * 100.0).toInt()
    val absolute = oldPrice - newPrice

    override fun compareTo(other: Double): Int = (oldPrice - newPrice).compareTo(other)
}