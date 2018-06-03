package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.texteffect.Ansi
import org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import org.tools4j.fixgrep.texteffect.MiscTextEffect

/**
 * User: ben
 * Date: 9/05/2018
 * Time: 6:43 AM
 */
class Color16Demo {
    val consoleIntro = "Below (should) be a list of foreground and background names, colored appropriately. If you see these colors, then your console probably supports 16 color escape codes.  In that case you can use the Fg or Bg prefixes to specify highlights.  e.g. --highlights=35:BgBrightCyan:line would format a fix line a bit like this \u001B[46;1m35=D|11=ABC|55=AUD/USD\u001B[m\n\n"

    val demo: String by lazy {
        val sb = StringBuilder()
        sb.append(MiscTextEffect.Bold.consoleTextBefore + "Foreground colors:\n" + Ansi.Normal)
        sb.append(Ansi16ForegroundColor.listForConsole()).append("\n\n")
        sb.append(MiscTextEffect.Bold.consoleTextBefore + "Background colors:\n" + Ansi.Normal)
        sb.append(Ansi16BackgroundColor.listForConsole()).append("\n")
        sb.toString()
    }
    val demoForConsole = consoleIntro + demo

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val demo = Color16Demo()
            println(demo.consoleIntro + demo.demo)
        }
    }
}