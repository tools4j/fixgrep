package org.tools4j.fixgrep.help

import java.awt.Desktop
import java.net.URL

/**
 * User: benjw
 * Date: 30/10/2018
 * Time: 06:18
 */
class OnlineManPage(val onlineHelpUrl: String) {
    fun launch() {
        val uri = URL(onlineHelpUrl).toURI()
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri)
        } else {
            val runtime = Runtime.getRuntime()
            runtime.exec("/usr/bin/firefox -new-window " + uri.toString())
        }
    }
}