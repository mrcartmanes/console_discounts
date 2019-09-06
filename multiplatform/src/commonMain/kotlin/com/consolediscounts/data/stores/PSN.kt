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
            Platform.PS4 to "$url/ru-ru/grid/STORE-MSF75508-PS4CAT/${page.getValue(
                Platform.PS4
            )}?gameContentType=games&platform=ps4"
        )
    override val pageSelector = "div.grid-header__center > div > div > a.paginator-control__page-number"
    override val gameNameSelector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * span[title]"
    override val gameUrlSelector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > a[href]"
    override val price1Selector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * h3:contains(RUB)"
    override val price2Selector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * div:not(:has(*)):contains(RUB)"
    override val posterSelector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * img.product-image__img.product-image__img--product.product-image__img-main[src]"
}