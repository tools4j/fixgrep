package org.tools4j.fixgrep

import mu.KLogging
import org.tools4j.fixgrep.help.*
import org.tools4j.fixgrep.html.HtmlPageFooter
import org.tools4j.fixgrep.html.HtmlPageHeader
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

            val isFullPageHtml = config.hasProperty(Option.html)
                    && (config.getAsBoolean(Option.man, false)
                            || config.hasPropertyAndIsNotFalse(Option.to_file)
                            || config.getAsString(Option.html, "") == "page"
                            || config.hasPropertyAndIsNotFalse(Option.launch_browser));

            if(isFullPageHtml){
                if(config.getAsBoolean(Option.man, false)){
                    HtmlPageHeader("fixgrep Man Page", true, false).write(printStream)
                } else {
                    val heading = "fixgrep " + configAndArguments.originalApplicationArguments.joinToString(" ")
                    HtmlPageHeader(heading, false, true).write(printStream)
                }
            }

            if(config.getAsBoolean(Option.color_demo_256, false)){
                printStream.println(Color256ConsoleDemo().demoForConsole)
            } else if(config.getAsBoolean(Option.color_demo_16, false)){
                printStream.println(Color16ConsoleDemo().demoForConsole)
            } else if(config.getAsBoolean(Option.man, false)){
                val docWriterFactory = if(config.hasPropertyAndIsNotFalse(Option.html)) DocWriterFactory.Html else DocWriterFactory.ConsoleText
                printStream.println(ManGenerator(docWriterFactory, configAndArguments, config.getAsBoolean(Option.debug, false)).man)
            } else if(config.getAsBoolean(Option.help, false)){
                HelpGenerator().go(outputStream);
            } else if(config.getAsBoolean(Option.install, false)){
                ExampleAppPropertiesFileCreator().createIfNecessary()
            } else if(config.getAsBoolean(Option.piped_input, false) && inputStream != null){
                readFromPipedInput()
            } else {
                readFromFiles(configAndArguments.arguments)
            }

            if(isFullPageHtml){
                HtmlPageFooter().write(printStream)
            }
        } finally {
            outputStream.flush()
            outputStream.close()
        }
    }

    private fun readFromFiles(arguments: List<*>) {
        if(arguments.isEmpty()){
            System.err.println("File list empty.  Must received piped input, or specify one or more files as arguments.")
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