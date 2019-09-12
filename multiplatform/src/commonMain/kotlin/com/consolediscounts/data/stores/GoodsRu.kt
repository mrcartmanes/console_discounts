package com.consolediscounts.data.stores

import com.consolediscounts.data.interfaces.IHtmlParser
import com.consolediscounts.data.interfaces.IURLDownload
import com.consolediscounts.entities.Platform

class GoodsRu(urlDownload: IURLDownload, htmlParser: IHtmlParser) : HtmlParseStore(urlDownload, htmlParser) {

    override val supportedPlatforms =
        listOf(Platform.PS4, Platform.XboxOne, Platform.NintendoSwitch)
    override val name = "GOODS.RU"
    override val currency = "\u20BD"
    override val url = "https://goods.ru"
    override val gamePrefix
        get() = mapOf(
            Platform.PS4 to "Игра для PlayStation 4 ",
            Platform.XboxOne to "Игра для Xbox One ",
            Platform.NintendoSwitch to "Игра для Nintendo Switch "
        )
    override val pageURL
        get() = mapOf(
            Platform.PS4 to "$url/catalog/igry-dlya-playstation/set-igry-na-ps-4/page-${page.getValue(Platform.PS4)}",
            Platform.XboxOne to "$url/catalog/igry-dlya-xbox/set-igry-na-xbox-one/page-${page.getValue(Platform.XboxOne)}",
            Platform.NintendoSwitch to "$url/catalog/igry-dlya-nintendo-switch/page-${page.getValue(Platform.NintendoSwitch)}"
        )
    override val pageSelector = "a[data-page]"
    override val gameNameSelector = "article.card-prod.card-prod-grid:has(div.previous-price) > header"
    override val gameUrlSelector =
        "article.card-prod.card-prod-grid:has(div.previous-price) > a.card-prod--slider[href]"
    override val priceSelector = "article.card-prod.card-prod-grid:has(div.previous-price) > div.favorite.card-prod--price"
    override val posterSelector = "article.card-prod.card-prod-grid:has(div.previous-price) > * span:nth-child(1) > img[src]"

    override fun extractPrices(s: String): Pair<Double?, Double?> =
        "([0-9]*).*\\?([0-9]*)".toRegex().find(s.filterNot { it == ' ' })?.let {
            it.groupValues[1].toDoubleOrNull() to it.groupValues[2].toDoubleOrNull()
        } ?: null to null
}