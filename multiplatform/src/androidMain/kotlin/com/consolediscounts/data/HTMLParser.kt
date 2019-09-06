package com.consolediscounts.data

import com.consolediscounts.data.interfaces.IHtmlParser
import org.jsoup.Jsoup

class HtmlParser : IHtmlParser {

    override fun get(html: String, selector: String): List<String> {
        val doc = Jsoup.parse(html)
        val attr = "(.*)\\[(.*)\\]".toRegex().find(selector)?.let { it.groupValues[2] }
        return doc.select(selector).map { if (attr == null) it.text() else it.attr(attr) }
    }
}