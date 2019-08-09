package com.psdiscounts

import com.psdiscounts.data.HtmlParser
import com.psdiscounts.data.URLDownload
import com.psdiscounts.data.interfaces.IHtmlParser
import com.psdiscounts.data.interfaces.IURLDownload
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

actual val kodeinPlatformModule = Kodein.Module("android") {
    bind<IURLDownload>() with singleton { URLDownload() }
    bind<IHtmlParser>() with singleton { HtmlParser() }
}