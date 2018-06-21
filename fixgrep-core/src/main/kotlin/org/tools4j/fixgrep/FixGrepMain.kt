package org.tools4j.fixgrep

import org.tools4j.fixgrep.utils.OutputFile
import java.awt.Desktop
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.URL


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

        if(configAndArguments.arguments.size >= 2 && configAndArguments.arguments.get(0) == "man" && configAndArguments.arguments.get(1) == "online"){
            val uri = URL("http://www.microsoft.com").toURI()
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(uri)
            } else {
                val runtime = Runtime.getRuntime()
                runtime.exec("/usr/bin/firefox -new-window " + uri.toString())
            }
            return
        }

        val fixGrepOutputStream: OutputStream
        val writingToFile = configAndArguments.config.hasPropertyAndIsNotFalse("to.file")
        val outputFile: OutputFile?

        if(writingToFile){
            val isHtml = configAndArguments.config.hasPropertyAndIsNotFalse("html")
            if(configAndArguments.config.hasPropertyAndIsTrueOrNull("to.file")){
                val extension = if(isHtml) OutputFile.Extension.html else OutputFile.Extension.log
                outputFile = OutputFile(extension)
            } else {
                val toFile = configAndArguments.config.getAsString("to.file")
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