package org.tools4j.fixgrep

import mu.KLogging
import org.tools4j.fixgrep.help.Color16ConsoleDemo
import org.tools4j.fixgrep.help.Color256ConsoleDemo
import org.tools4j.fixgrep.help.DocWriterFactory
import org.tools4j.fixgrep.help.ExampleAppPropertiesFileCreator
import org.tools4j.fixgrep.help.HelpGenerator
import org.tools4j.fixgrep.help.ManGenerator
import org.tools4j.fixgrep.html.HtmlPageFooter
import org.tools4j.fixgrep.html.HtmlPageHeader
import org.tools4j.properties.ConfigAndArguments
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream


/**
 * User: ben
 * Date: 12/03/2018
 * Time: 7:00 AM
 */
class FixGrep(val inputStream: InputStream?, val outputStream: OutputStream, val configAndArguments: ConfigAndArguments) {
    companion object: KLogging()

    val formatter: Formatter by lazy {
        Formatter(FormatSpec(configAndArguments.config))
    }

    val printStream: PrintStream by lazy {
        PrintStream(outputStream)
    }

    fun go() {
        try {
            val config = configAndArguments.config

            if(config.hasProperty("html") && config.getAsString("html", "page") == "page"){
                if(config.getAsBoolean("man", false)){
                    HtmlPageHeader("fixgrep Man Page", true).write(printStream)
                } else {
                    val heading = "fixgrep " + configAndArguments.originalApplicationArguments.joinToString(" ")
                    HtmlPageHeader(heading).write(printStream)
                }
            }

            if(config.getAsBoolean("256.color.demo", false)){
                printStream.println(Color256ConsoleDemo().demoForConsole)
            } else if(config.getAsBoolean("16.color.demo", false)){
                printStream.println(Color16ConsoleDemo().demoForConsole)
            } else if(config.getAsBoolean("man", false)){
                val docWriterFactory = if(config.hasPropertyAndIsNotFalse("html")) DocWriterFactory.Html else DocWriterFactory.ConsoleText
                printStream.println(ManGenerator(docWriterFactory, configAndArguments, config.getAsBoolean("debug", false)).man)
            } else if(config.getAsBoolean("help", false)){
                HelpGenerator().go(outputStream);
            } else if(config.getAsBoolean("install", false)){
                ExampleAppPropertiesFileCreator().createIfNecessary()
            } else if(config.getAsBoolean("piped.input", false) && inputStream != null){
                readFromPipedInput()
            } else {
                readFromFiles(configAndArguments.arguments)
            }

            if(config.hasProperty("html") && config.getAsString("html", "page") == "page"){
                HtmlPageFooter().write(printStream)
            }
        } finally {
            outputStream.flush()
            outputStream.close()
        }
    }

    private fun readFromFiles(arguments: List<*>) {
        if(arguments.isEmpty()){
            System.err.println("File list empty.  Must received piped input, or specify one or more files as arguments")
            HelpGenerator().go(outputStream);
            return
        }
        logger.info("About to read from files $arguments")
        for(obj in arguments){
            if(obj == null) continue
            val str = obj as CharSequence
            if(str.isEmpty()) continue
            if(str.startsWith("-")){
                System.err.println("Invalid option $str")
                HelpGenerator().go(outputStream);
                return
            }
            val file = File(str.toString())
            if(!file.exists()){
                System.err.println("File at location: [" + file.absolutePath + "] does not exist.")
                return
            }
            readFromBufferedReader(file.bufferedReader())
        }
    }

    private fun readFromPipedInput() {
        val reader = inputStream!!.bufferedReader()
        logger.info("About to read from piped input")
        readFromBufferedReader(reader)
    }

    private fun readFromBufferedReader(reader: BufferedReader) {
        while (true) {
            val line = reader.readLine()
            if (line == null) break
            else handleLine(line)
        }
    }

    private fun handleLine(line: String) {
        val matcher = formatter.logLineRegexPattern.matcher(line)
        if (matcher.find()) {
            val formattedLine = formatter.format(matcher)
            if(formattedLine != null){
                printStream.println(formattedLine)
            }
        }
    }
}