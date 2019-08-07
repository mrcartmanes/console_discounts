package com.psdiscounts.data

import com.psdiscounts.data.interfaces.IURLDownload
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class URLDownload : IURLDownload {

    private val logger = KotlinLogging.logger {}
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun <T> download(url: String, f: (ResponseBody) -> T): T? {
        val request = Request.Builder()
            .url(url)
            .build()
        return try {
            client.newCall(request).execute().body()?.let(f)
        } catch (e: IOException) {
            logger.error(e) { "Download failed" }
            null
        }
    }

    override fun downloadText(url: String): String? = download(url) { it.string() }
    override fun downloadImage(url: String): ByteArray? = download(url) { it.bytes() }
}