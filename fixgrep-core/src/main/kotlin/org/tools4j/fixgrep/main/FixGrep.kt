package org.tools4j.fixgrep.main

import mu.KLogging
import org.tools4j.fixgrep.config.ConfigAndArguments
import org.tools4j.fixgrep.config.Option
import org.tools4j.fixgrep.formatting.FormatSpec
import org.tools4j.fixgrep.formatting.Formatter
import org.tools4j.fixgrep.help.*
import org.tools4j.fixgrep.html.HtmlPageFooter
import org.tools4j.fixgrep.html.HtmlPageHeader
import org.tools4j.fixgrep.linehandlers.DefaultFixLineHandler
import org.tools4j.fixgrep.linehandlers.DefaultTextLineHandler
import org.tools4j.fixgrep.linehandlers.FixLineHandler
import org.tools4j.fixgrep.linehandlers.LineHandler
import org.tools4j.fixgrep.orders.*
import java.io.*
import java.util.function.Consumer


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

    val fixLineHandler: FixLineHandler by lazy {
        if(configAndArguments.config.hasPropertyAndIsNotFalse(Option.group_by_order)){
            OrderGroupingFixLineHandler(
                    formatter,
                    UniqueIdSpecs(
                        UniqueClientOrderIdSpec(),
                        UniqueOriginalClientOrderIdSpec(),
                        UniqueOrderIdSpec()),
                    IdFilter(configAndArguments.config.getAsStringList(Option.group_by_order, null)),
                    Consumer {printStream.print(it)})
        } else {
            DefaultFixLineHandler(formatter, Consumer { printStream.print(it) })
        }
    }

    val textLineHandler: LineHandler by lazy {
        DefaultTextLineHandler(formatter.spec, fixLineHandler)
    }

    fun go() {
        try {
            val config = configAndArguments.config

            val isFullPageHtml = config.hasProperty(Option.html)
                    && (config.getAsBoolean(Option.man, false)
                    || config.hasPropertyAndIsNotFalse(Option.to_file)
                    || config.getAsString(Option.html, "") == "page"
                    || config.hasPropertyAndIsNotFalse(Option.launch_browser));

            if (isFullPageHtml) {
                if (config.getAsBoolean(Option.man, false)) {
                    HtmlPageHeader("fixgrep Man Page", true, false).write(printStream)
                } else {
                    val heading = "fixgrep " + configAndArguments.originalApplicationArguments.joinToString(" ")
                    HtmlPageHeader(heading, false, true).write(printStream)
                }
            }

            if (config.getAsBoolean(Option.color_demo_256, false)) {
                printStream.println(Color256ConsoleDemo().demoForConsole)
            } else if (config.getAsBoolean(Option.color_demo_16, false)) {
                printStream.println(Color16ConsoleDemo().demoForConsole)
            } else if (config.getAsBoolean(Option.man, false)) {
                val docWriterFactory = if (config.hasPropertyAndIsNotFalse(Option.html)) DocWriterFactory.Html else DocWriterFactory.ConsoleText
                printStream.println(ManGenerator(docWriterFactory, configAndArguments, config.getAsBoolean(Option.debug, false)).man)
            } else if (config.getAsBoolean(Option.help, false)) {
                HelpGenerator().go(outputStream);
            } else if (config.getAsBoolean(Option.install, false)) {
                ExampleAppPropertiesFileCreator().createIfNecessary()
            } else if (config.getAsBoolean(Option.piped_input, false) && inputStream != null) {
                readFromPipedInput()
            } else {
                readFromFiles(configAndArguments.arguments)
            }

            if (isFullPageHtml) {
                HtmlPageFooter().write(printStream)
            }
        } catch (e: Throwable){
            e.printStackTrace(printStream)
        } finally {
            printStream.flush()
            printStream.close()
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
            file.bufferedReader().use {
                readFromBufferedReader(it)
            }
        }
    }

    private fun readFromPipedInput() {
        val reader = inputStream!!.bufferedReader()
        try {
            logger.info("About to read from piped input")
            readFromBufferedReader(reader)
        } finally {
            reader.close()
        }
    }

    private fun readFromBufferedReader(reader: BufferedReader) {
        while (true) {
            val line = reader.readLine()
            if (line == null) break
            else textLineHandler.handle(line)
        }
        textLineHandler.finish()
    }
}