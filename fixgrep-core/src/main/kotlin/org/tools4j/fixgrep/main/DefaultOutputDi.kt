package org.tools4j.fixgrep.main

import mu.KLogging
import org.tools4j.fixgrep.utils.OutputFile
import java.io.OutputStream
import java.io.PrintStream

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 06:46
 */
class DefaultOutputDi(val diContext: DiContext): OutputDi {
    companion object: KLogging()

    override val outputStream: OutputStream by lazy {
        val outputStream: OutputStream
        val writingToFile = diContext.config.outputToFileWithGivenName || diContext.config.outputToFileWithGeneratedName || diContext.config.launchInBrowser

        if(writingToFile){
            logger.info { "Writing to file" }
            val outputFile: OutputFile
            if(!diContext.config.outputToFileWithGivenName){
                val isHtml = diContext.config.htmlFormatting
                val extension = if(isHtml) OutputFile.Extension.html else OutputFile.Extension.log
                outputFile = OutputFile(extension)
                logger.info { "Writing to generated filename: ${outputFile.fileObj.absolutePath}" }
            } else {
                val toFile = diContext.config.outputFileName!!
                outputFile = OutputFile(toFile)
                logger.info { "Writing to specified filename: $toFile" }
            }
            outputStream = outputFile.outputStream
            diContext.addShutdown { outputStream.flush(); outputStream.close() }
            diContext.addShutdown { outputFile.finish() }
            if (diContext.config.launchInBrowser) {
                diContext.addShutdown { outputFile.launchInBrowser() }
            }
        } else {
            outputStream = System.out
            diContext.addShutdown { outputStream.flush(); outputStream.close() }
        }
        outputStream
    }

    override val printStream: PrintStream by lazy {
        val printStream = PrintStream(outputStream)
        printStream
    }
}