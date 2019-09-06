package com.consolediscounts.data.interfaces

interface IURLDownload {
    fun downloadImage(url: String): ByteArray?
    fun downloadText(url: String): String?
}