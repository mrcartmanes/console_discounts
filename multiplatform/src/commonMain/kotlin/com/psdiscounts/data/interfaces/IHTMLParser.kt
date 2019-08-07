package com.psdiscounts.data.interfaces

interface IHtmlParser {
    fun get(html: String, selector: String): List<String>
}