package com.consolediscounts.data.stores

import com.consolediscounts.data.interfaces.IHtmlParser
import com.consolediscounts.data.interfaces.IURLDownload
import com.consolediscounts.entities.Platform

class PSN(urlDownload: IURLDownload, htmlParser: IHtmlParser) : HtmlParseStore(urlDownload, htmlParser) {

    override val supportedPlatforms = listOf(Platform.PS4)
    override val name = "PSN"
    override val url = "https://store.playstation.com"
    override val gamePrefix = mapOf(Platform.PS4 to "")
    override val pageURL
        get() = mapOf(
            Platform.PS4 to "$url/ru-ru/grid/STORE-MSF75508-PS4CAT/${page.getValue(Platform.PS4)}?gameContentType=games&platform=ps4"
        )
    override val pageSelector = "div.grid-header__center > div > div > a.paginator-control__page-number"
    override val gameNameSelector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * span[title]"
    override val gameUrlSelector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > a[href]"
    override val priceSelector = "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * span.grid-cell__prices-container"
    override val posterSelector = "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * img.product-image__img.product-image__img--product.product-image__img-main[src]"

    override fun extractPrices(s: String): Pair<Double?, Double?> =
        "RUB ([0-9]*) RUB ([0-9]*)".toRegex().find(s.filterNot { it == '.' })?.let {
            it.groupValues[1].toDoubleOrNull() to it.groupValues[2].toDoubleOrNull()
        } ?: null to null
}