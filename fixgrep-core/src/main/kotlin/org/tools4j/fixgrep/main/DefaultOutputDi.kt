package org.tools4j.fixgrep.main

import org.tools4j.fixgrep.config.FixGrepConfig
import org.tools4j.fixgrep.config.Option
import org.tools4j.fixgrep.utils.OutputFile
import java.io.OutputStream
import java.io.PrintStream

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 06:46
 */
class DefaultOutputDi(val diContext: DiContext): OutputDi {
    override val outputStream: OutputStream by lazy {
        val outputStream: OutputStream
        val writingToFile = diContext.config.outputToFile || diContext.config.launchInBrowser

        if(writingToFile){
            val fileNameNotGiven = diContext.config.outputToFileButFilenameNotGiven
            val outputFile: OutputFile
            if(fileNameNotGiven){
                val isHtml = diContext.config.htmlFormatting
                val extension = if(isHtml) OutputFile.Extension.html else OutputFile.Extension.log
                outputFile = OutputFile(extension)
            } else {
                val toFile = diContext.config.outputFileName
                outputFile = OutputFile(toFile)
                diContext.addShutdown {
                    outputFile.finish()
                    if (diContext.config.launchInBrowser) {
                        outputFile.launchInBrowser()
                    }
                }
            }
            outputStream = outputFile.outputStream
        } else {
            outputStream = System.out
        }
        outputStream
    }

    override val printStream: PrintStream by lazy {
        val printStream = PrintStream(outputStream)
        printStream
    }
}