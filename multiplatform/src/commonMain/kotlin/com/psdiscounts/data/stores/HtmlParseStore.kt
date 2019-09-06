package com.psdiscounts.data.stores

import com.psdiscounts.data.interfaces.IHtmlParser
import com.psdiscounts.data.interfaces.IURLDownload
import com.psdiscounts.data.isDigit
import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.entities.Discount
import com.psdiscounts.entities.Platform
import kotlin.math.max
import kotlin.math.min

abstract class HtmlParseStore(private val urlDownload: IURLDownload, private val htmlParser: IHtmlParser) : IStore {

    protected abstract val url: String
    protected abstract val pageSelector: String
    protected abstract val gameNameSelector: String
    protected abstract val gameUrlSelector: String
    protected abstract val price1Selector: String
    protected abstract val price2Selector: String
    protected abstract val posterSelector: String

    protected abstract val gamePrefix: Map<Platform, String>
    protected abstract val pageURL: Map<Platform, String>

    protected var page: MutableMap<Platform, Int> =
        Platform.values().map { it to 1 }.toMap().toMutableMap()
    private var discountsPerPage: MutableMap<Platform, MutableList<Discount>> =
        Platform.values().map { it to mutableListOf<Discount>() }.toMap().toMutableMap()

    override fun getDiscounts(platform: Platform): Sequence<Discount> {
        page[platform] = 1
        return generateSequence {
            while (discountsPerPage.getValue(platform).isEmpty()) {
                val html = urlDownload.downloadText(pageURL.getValue(platform)) ?: ""
                val pagesCount = htmlParser.get(
                    html,
                    pageSelector
                ).filterNot { it.isEmpty() }.map { it.toInt() }.max() ?: 0

                if (page.getValue(platform) > pagesCount) break

                val games =
                    htmlParser.get(html, gameNameSelector)
                        .map { it.removePrefix(gamePrefix[platform] ?: "") }
                val urls = htmlParser.get(html, gameUrlSelector).map { url + it }
                val prices1 = htmlParser.get(html, price1Selector)
                    .map { it.filter { c -> isDigit(c) }.toDoubleOrNull() }
                val prices2 = htmlParser.get(html, price2Selector)
                    .map { it.filter { c -> isDigit(c) }.toDoubleOrNull() }
                val posters =
                    htmlParser.get(html, posterSelector).map { urlDownload.downloadImage(it) }

                discountsPerPage[platform] = games.mapIndexed { i, game ->
                        val price1 = prices1.getOrNull(i)
                        val price2 = prices2.getOrNull(i)
                        val poster = posters.getOrNull(i)
                        val url = urls.getOrNull(i)
                        if (price2 == null || price1 == null || game.isEmpty()) null
                        else Discount(
                            name,
                            game,
                            platform,
                            url,
                            poster,
                            oldPrice = max(price1, price2),
                            newPrice = min(price1, price2)
                        )
                    }
                    .filterNotNull()
                    .toMutableList()

                page[platform] = page.getValue(platform) + 1
            }

            if (discountsPerPage.getValue(platform).isNotEmpty()) discountsPerPage.getValue(platform).removeAt(
                0
            )
            else null
        }
    }
}