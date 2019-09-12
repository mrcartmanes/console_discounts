package com.consolediscounts.data.stores

import com.consolediscounts.data.interfaces.IURLDownload
import com.consolediscounts.domain.interfaces.IStore
import com.consolediscounts.entities.Discount
import com.consolediscounts.entities.Platform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

class EShop(private val urlDownload: IURLDownload) : IStore {
    override val name = "eShop"
    override val currency = "\u20BD"
    override val supportedPlatforms = listOf(Platform.NintendoSwitch)

    @Serializable
    data class ServerResponse(val response: GamesWithDiscount)

    @Serializable
    data class GamesWithDiscount(val docs: List<Game>)

    @Serializable
    data class Game(
        val title: String,
        val url: String,
        @SerialName("image_url") val imageURL: String,
        @SerialName("nsuid_txt") val id: List<String>,
        @SerialName("system_names_txt") val systemNames: List<String>
    )

    @Serializable
    data class Price(
        @SerialName("raw_value") val value: String
    )

    @Serializable
    data class Discount(
        @SerialName("regular_price") val regularPrice: Price,
        @SerialName("discount_price") val discountPrice: Price,
        @SerialName("title_id") val gameID: Long
    )

    @Serializable
    data class Discounts(val prices: List<Discount>)

    private val discountsResuestURL = "https://searching.nintendo-europe.com/ru/select?q=*&fq=type%3AGAME%20AND%20((price_has_discount_b%3A%22true%22))&start=0&rows=9999&wt=json"
    private val pricesBaseURL = "https://api.ec.nintendo.com/v1/price?country=RU&lang=ru&ids="

    @UnstableDefault
    override fun getDiscounts(platform: Platform): Sequence<com.consolediscounts.entities.Discount> {
        urlDownload.downloadText(discountsResuestURL)?.let {
            val games = try {
                Json.nonstrict.parse(ServerResponse.serializer(), it).response.docs
            } catch (e: Exception) {
                listOf<Game>()
            }
            return games.asSequence().map { game ->
                urlDownload.downloadText(game.id.joinToString("%2C", pricesBaseURL))
                    ?.let { jsonPricesResponse ->
                        try {
                            val discount = Json.nonstrict.parse(Discounts.serializer(), jsonPricesResponse).prices.first()
                            Discount(
                                store = this,
                                game = game.title,
                                platform = platform,
                                url = "https://www.nintendo.ru" + game.url,
                                poster = urlDownload.downloadImage("https:" + game.imageURL),
                                oldPrice = discount.regularPrice.value.toDouble(),
                                newPrice = discount.discountPrice.value.toDouble()
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
            }.filterNotNull()
        }
        return emptySequence()
    }
}