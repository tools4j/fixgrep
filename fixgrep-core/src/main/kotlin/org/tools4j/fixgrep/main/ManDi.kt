package org.tools4j.fixgrep.main

import org.tools4j.fixgrep.help.DocWriterFactory
import org.tools4j.fixgrep.help.ManGenerator

/**
 * User: benjw
 * Date: 30/10/2018
 * Time: 06:26
 */
class ManDi(val diContext: DiContext, val outputDi: OutputDi) {
    init {
        diContext.addRunnable { go()}
    }

    fun go(){
        val docWriterFactory = if (diContext.config.htmlFormatting) DocWriterFactory.Html else DocWriterFactory.ConsoleText
        outputDi.printStream.println(ManGenerator(docWriterFactory, diContext.config, diContext.config.debugMode).man)
    }
}