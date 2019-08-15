package com.psdiscounts.data.stores

import com.psdiscounts.data.interfaces.IHtmlParser
import com.psdiscounts.data.interfaces.IURLDownload

class GoodsRu(urlDownload: IURLDownload, htmlParser: IHtmlParser) : HtmlParseStore(urlDownload, htmlParser) {

    override val name = "GOODS.RU"
    override val gamePrefix = "Игра для PlayStation 4 "
    override val pageURL
        get() = "https://goods.ru/catalog/igry-dlya-playstation/set-igry-na-ps-4/page-$page"
    override val pageSelector = "a[data-page]"
    override val gameNameSelector = "article.card-prod.card-prod-grid:has(div.previous-price) > header"
    override val price1Selector = "article.card-prod.card-prod-grid:has(div.previous-price) > * div.previous-price"
    override val price2Selector =
        "article.card-prod.card-prod-grid:has(div.previous-price) > * div.current.favoritePrice"
    override val posterSelector =
        "article.card-prod.card-prod-grid:has(div.previous-price) > * span:nth-child(1) > img[src]"
}