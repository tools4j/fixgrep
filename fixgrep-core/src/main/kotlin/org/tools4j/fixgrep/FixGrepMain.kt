package org.tools4j.fixgrep

import org.tools4j.fixgrep.utils.OutputFile
import java.io.File
import java.io.InputStream
import java.io.OutputStream


/**
 * User: ben
 * Date: 12/03/2018
 * Time: 7:00 AM
 */
class FixGrepMain(val inputStream: InputStream?, val outputStream: OutputStream, val args: List<String>){
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val fixGrepMain = FixGrepMain(System.`in`, System.out, args.toList())
            fixGrepMain.go()
        }
    }

    private fun go() {
        val configAndArguments = ConfigBuilder(args).configAndArguments
        val fixGrepOutputStream: OutputStream
        val writingToFile = configAndArguments.config.hasPropertyAndIsNotFalse("to.file")
        val outputFile: OutputFile?

        if(writingToFile){
            val toFile = configAndArguments.config.getAsString("to.file")
            val isHtml = configAndArguments.config.hasPropertyAndIsNotFalse("html")
            if(toFile == "true"){
                val extension = if(isHtml) OutputFile.Extension.html else OutputFile.Extension.log
                outputFile = OutputFile(extension)
            } else {
                outputFile = OutputFile(toFile)
            }
            fixGrepOutputStream = outputFile.outputStream
        } else {
            fixGrepOutputStream = outputStream
            outputFile = null
        }

        //Run fixgrep
        FixGrep(inputStream, fixGrepOutputStream, configAndArguments).go()

        if(writingToFile) {
            outputFile!!.finish()
            if (configAndArguments.config.hasPropertyAndIsNotFalse("launch.browser")) {
                outputFile.launchInBrowser()
            }
        }
    }
}