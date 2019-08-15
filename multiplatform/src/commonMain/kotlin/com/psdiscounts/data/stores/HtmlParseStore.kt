package com.psdiscounts.data.stores

import com.psdiscounts.data.interfaces.IHtmlParser
import com.psdiscounts.data.interfaces.IURLDownload
import com.psdiscounts.data.isDigit
import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.entities.Discount
import kotlin.math.max
import kotlin.math.min

abstract class HtmlParseStore(private val urlDownload: IURLDownload, private val htmlParser: IHtmlParser) : IStore {

    protected abstract val pageURL: String
    protected abstract val pageSelector: String
    protected abstract val gameNameSelector: String
    protected abstract val price1Selector: String
    protected abstract val price2Selector: String
    protected abstract val posterSelector: String
    protected abstract val gamePrefix: String
    protected var page = 1

    private var discountsPerPage: MutableList<Discount> = mutableListOf()
    private var html: String = ""
    private val pagesCount: Int
        get() {
            html = urlDownload.downloadText(pageURL) ?: ""
            return htmlParser.get(html, pageSelector).filterNot { it.isEmpty() }.map { it.toInt() }.max() ?: 0
        }

    override fun getDiscounts(): Sequence<Discount> = generateSequence {
        while (discountsPerPage.isEmpty() && page <= pagesCount) {
            val games = htmlParser.get(html, gameNameSelector).map { it.removePrefix(gamePrefix) }
            val prices1 = htmlParser.get(html, price1Selector).map { it.filter { c -> isDigit(c) }.toDoubleOrNull() }
            val prices2 = htmlParser.get(html, price2Selector).map { it.filter { c -> isDigit(c) }.toDoubleOrNull() }
            val posters = htmlParser.get(html, posterSelector)

            discountsPerPage = games
                .mapIndexed { i, game ->
                    val price1 = prices1.getOrNull(i)
                    val price2 = prices2.getOrNull(i)
                    val poster = posters.getOrNull(i)
                    if (price2 == null || price1 == null || game.isEmpty()) null
                    else Discount(
                        name,
                        game,
                        poster,
                        oldPrice = max(price1, price2),
                        newPrice = min(price1, price2)
                    )
                }
                .filterNotNull()
                .toMutableList()

            page++
        }

        if (discountsPerPage.isNotEmpty()) discountsPerPage.removeAt(0) else null
    }
}