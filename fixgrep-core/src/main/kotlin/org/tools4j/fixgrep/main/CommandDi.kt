package org.tools4j.fixgrep.main

import org.tools4j.fixgrep.help.*

/**
 * User: benjw
 * Date: 30/10/2018
 * Time: 17:00
 */

class CommandDi(diContext: DiContext, inputDi: InputDi, outputDi: OutputDi) {
    init {
        if (diContext.args.size >= 2 && diContext.args.get(0) == "man" && diContext.args.get(1) == "online") {
            diContext.addRunnable { OnlineManPage(diContext.config.onlineHelpUrl).launch() }
        } else if (diContext.config.colorDemo256) {
            diContext.addRunnable { outputDi.printStream.println(Color256ConsoleDemo().demoForConsole) }
        } else if (diContext.config.colorDemo16) {
            diContext.addRunnable { outputDi.printStream.println(Color16ConsoleDemo().demoForConsole) }
        } else if (diContext.config.man) {
            ManDi(diContext, outputDi)
        } else if (diContext.config.help) {
            diContext.addRunnable { HelpGenerator(outputDi.outputStream).go() }
        } else if (diContext.config.install) {
            diContext.addRunnable { ExampleAppPropertiesFileCreator().createIfNecessary() }
        } else {
            FormattingDi(diContext, inputDi, outputDi)
        }
    }
}