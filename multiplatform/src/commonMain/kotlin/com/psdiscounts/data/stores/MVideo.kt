package com.psdiscounts.data.stores

import com.psdiscounts.data.interfaces.IHtmlParser
import com.psdiscounts.data.interfaces.IURLDownload
import com.psdiscounts.data.isDigit
import com.psdiscounts.domain.interfaces.IStore
import com.psdiscounts.entities.Discount

/*
    URL for PS4 games: https://www.mvideo.ru/playstation-4327/ps4-igry-4331/f/category=igry-dlya-playstation-4-ps4-4343?page=N, where's N - page number
*/
class MVideo(private val urlDownload: IURLDownload, private val htmlParser: IHtmlParser, name: String) : IStore {

    private val storeName: String = name
    private var discountsPerPage: MutableList<Discount> = mutableListOf()
    private var page = 1
    private val pagesCount by lazy {
        val html =
            urlDownload.downloadText("https://www.mvideo.ru/playstation-4327/ps4-igry-4331/f/category=igry-dlya-playstation-4-ps4-4343?page=1")
                ?: ""
        htmlParser.get(html, "a.c-pagination__num.c-btn.c-btn_white").lastOrNull()?.toInt() ?: 0
    }

    override fun getDiscounts(): Sequence<Discount> = Sequence {
        object : AbstractIterator<Discount>() {
            override fun computeNext() {
                while (discountsPerPage.isEmpty() && page <= pagesCount) {
                    val html =
                        urlDownload.downloadText("https://www.mvideo.ru/playstation-4327/ps4-igry-4331/f/category=igry-dlya-playstation-4-ps4-4343?page=$page")
                            ?: ""
                    val names = htmlParser.get(html, "div.c-product-tile__description-wrapper > h4 > a")
                        .map { it.removePrefix("PS4 игра ") }
                    val prices =
                        htmlParser.get(html, "div.c-pdp-price__current").map { it.filter { c -> isDigit(c) } }
                    val oldPrices =
                        htmlParser.get(html, "span.u-mr-4.c-pdp-price__old")
                            .map { it.filter { c -> isDigit(c) } }
                    val posters =
                        htmlParser.get(
                            html,
                            "div.c-product-tile-picture > div > a > div > div > img[data-original]"
                        )

                    discountsPerPage = oldPrices
                        .mapIndexed { i, s ->
                            val game = names.getOrNull(i)
                            val price = prices.getOrNull(i)
                            val poster = posters.getOrNull(i)
                            if (game == null || price == null || s.isEmpty()) null
                            else Discount(storeName, game, poster, s.toDouble(), price.toDouble())
                        }
                        .filterNotNull()
                        .toMutableList()

                    page++
                }

                if (discountsPerPage.isNotEmpty()) setNext(discountsPerPage.removeAt(0))
                else done()
            }
        }
    }
}