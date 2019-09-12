package com.consolediscounts.data.stores

import com.consolediscounts.data.interfaces.IHtmlParser
import com.consolediscounts.data.interfaces.IURLDownload
import com.consolediscounts.entities.Platform

class Microsoft(urlDownload: IURLDownload, htmlParser: IHtmlParser) : HtmlParseStore(urlDownload, htmlParser) {
    override val name = "Microsoft"
    override val currency = "$"
    override val supportedPlatforms = listOf(Platform.XboxOne)
    override val url = "https://www.microsoft.com"
    override val pageURL: Map<Platform, String>
        get() = mapOf(
            Platform.XboxOne to "$url/ru-ru/store/deals/games/xbox?s=store&skipitems=${page.getValue(Platform.XboxOne).minus(1).times(90)}"
        )
    override val gamePrefix: Map<Platform, String>
        get() = mapOf(
            Platform.XboxOne to ""
        )
    override val pageSelector = "ul.m-pagination > li > *"
    override val gameNameSelector = "div.m-channel-placement-item > * h3.c-subheading-6"
    override val gameUrlSelector = "div.m-channel-placement-item > a[href]"
    override val priceSelector = "div.m-channel-placement-item > * div.c-channel-placement-price"
    override val posterSelector = "div.m-channel-placement-item > * img[data-src]"

    override fun extractPrices(s: String): Pair<Double?, Double?> =
        ".*USD\\$([0-9.]*).*USD\\\$([0-9.]*)".toRegex().find(s)?.let {
            it.groupValues[1].toDoubleOrNull() to it.groupValues[2].toDoubleOrNull()
        } ?: null to null
}