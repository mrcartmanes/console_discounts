package com.psdiscounts.data.stores

import com.psdiscounts.data.interfaces.IHtmlParser
import com.psdiscounts.data.interfaces.IURLDownload

class PSN(urlDownload: IURLDownload, htmlParser: IHtmlParser) : HtmlParseStore(urlDownload, htmlParser) {

    override val name = "PSN"
    override val gamePrefix = ""
    override val pageURL
        get() = "https://store.playstation.com/ru-ru/grid/STORE-MSF75508-PS4CAT/$page?gameContentType=games&platform=ps4"
    override val pageSelector = "div.grid-header__center > div > div > a.paginator-control__page-number"
    override val gameNameSelector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * span[title]"
    override val price1Selector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * h3:contains(RUB)"
    override val price2Selector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * div:not(:has(*)):contains(RUB)"
    override val posterSelector =
        "div.grid-cell.grid-cell--game:has(span.discount-badge__message:contains(%)) > * img.product-image__img.product-image__img--product.product-image__img-main[src]"
}