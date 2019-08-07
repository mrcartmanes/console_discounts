package com.psdiscounts.data.interfaces

interface IURLDownload {
    fun downloadImage(url: String): ByteArray?
    fun downloadText(url: String): String?
}