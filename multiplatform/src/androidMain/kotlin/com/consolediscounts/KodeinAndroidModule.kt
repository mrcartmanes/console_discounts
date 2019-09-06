package com.consolediscounts

import com.consolediscounts.data.HtmlParser
import com.consolediscounts.data.URLDownload
import com.consolediscounts.data.interfaces.IHtmlParser
import com.consolediscounts.data.interfaces.IURLDownload
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

actual val kodeinPlatformModule = Kodein.Module("android") {
    bind<IURLDownload>() with provider { URLDownload() }
    bind<IHtmlParser>() with provider { HtmlParser() }
}