package com.consolediscounts.entities

data class Discount(
    val store: String,
    val game: String,
    val platform: Platform,
    val url: String?,
    val poster: ByteArray?,
    val oldPrice: Double,
    val newPrice: Double
) : Comparable<Double> {

    val percent = if (oldPrice == 0.0) 0 else ((oldPrice - newPrice / oldPrice) * 100.0).toInt()
    val absolute = oldPrice - newPrice

    override fun compareTo(other: Double): Int = (oldPrice - newPrice).compareTo(other)
    override fun equals(other: Any?): Boolean = (other as? Discount?)?.game == game
    override fun hashCode(): Int = game.hashCode()
    override fun toString(): String = "Discount(store=$store, game=$game, oldPrice=$oldPrice, newPrice=$newPrice)"
}