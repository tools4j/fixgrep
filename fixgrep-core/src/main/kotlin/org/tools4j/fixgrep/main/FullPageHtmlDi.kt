package org.tools4j.fixgrep.main

import org.tools4j.fixgrep.html.HtmlPageFooter
import org.tools4j.fixgrep.html.HtmlPageHeader

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 06:53
 */
class FullPageHtmlDi(diContext: DiContext, outputDi: OutputDi){
    init{
        if (diContext.config.htmlPageFormatting) {
            if (diContext.config.man) {
                HtmlPageHeader("fixgrep Man Page", true, false).write(outputDi.printStream)
            } else {
                val heading = "fixgrep " + diContext.args.joinToString(" ")
                HtmlPageHeader(heading, false, true).write(outputDi.printStream)
            }
            diContext.addShutdown { HtmlPageFooter().write(outputDi.printStream) }
        }
    }
}