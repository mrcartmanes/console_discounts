package com.psdiscounts.data.stores

import com.psdiscounts.data.interfaces.IHtmlParser
import com.psdiscounts.data.interfaces.IURLDownload
import com.psdiscounts.entities.Platform

class GoodsRu(urlDownload: IURLDownload, htmlParser: IHtmlParser) : HtmlParseStore(urlDownload, htmlParser) {

    override val supportedPlatforms =
        listOf(Platform.PS4, Platform.XboxOne, Platform.NintendoSwitch)
    override val name = "GOODS.RU"
    override val url = "https://goods.ru"
    override val gamePrefix
        get() = mapOf(
            Platform.PS4 to "Игра для PlayStation 4 ",
            Platform.XboxOne to "Игра для Xbox One ",
            Platform.NintendoSwitch to "Игра для Nintendo Switch "
        )
    override val pageURL
        get() = mapOf(
            Platform.PS4 to "$url/catalog/igry-dlya-playstation/set-igry-na-ps-4/page-${page.getValue(
                Platform.PS4
            )}",
            Platform.XboxOne to "$url/catalog/igry-dlya-xbox/set-igry-na-xbox-one/page-${page.getValue(
                Platform.XboxOne
            )}",
            Platform.NintendoSwitch to "$url/catalog/igry-dlya-nintendo-switch/page-${page.getValue(
                Platform.NintendoSwitch
            )}"
        )
    override val pageSelector = "a[data-page]"
    override val gameNameSelector = "article.card-prod.card-prod-grid:has(div.previous-price) > header"
    override val gameUrlSelector =
        "article.card-prod.card-prod-grid:has(div.previous-price) > a.card-prod--slider[href]"
    override val price1Selector = "article.card-prod.card-prod-grid:has(div.previous-price) > * div.previous-price"
    override val price2Selector =
        "article.card-prod.card-prod-grid:has(div.previous-price) > * div.current.favoritePrice"
    override val posterSelector =
        "article.card-prod.card-prod-grid:has(div.previous-price) > * span:nth-child(1) > img[src]"
}