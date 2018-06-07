package org.tools4j.fixgrep

import org.tools4j.fixgrep.help.Color16Demo
import org.tools4j.fixgrep.help.Color256Demo
import org.tools4j.fixgrep.help.DocWriterFactory
import org.tools4j.fixgrep.help.ExampleAppPropertiesFileCreator
import org.tools4j.fixgrep.help.HelpGenerator
import org.tools4j.fixgrep.help.ManGenerator
import org.tools4j.properties.ConfigAndArguments
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream


/**
 * User: ben
 * Date: 12/03/2018
 * Time: 7:00 AM
 */
class FixGrep(val inputStream: InputStream, val outputStream: OutputStream, val configAndArguments: ConfigAndArguments) {

    constructor(inputStream: InputStream, outputStream: OutputStream, args: Array<String>)
            : this(inputStream, outputStream, ConfigBuilder(args).configAndArguments)

    val formatter: Formatter by lazy {
        Formatter(FormatSpec(configAndArguments.config))
    }

    val printStream: PrintStream by lazy {
        PrintStream(outputStream)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val fixGrep = FixGrep(System.`in`, System.out, args)
            fixGrep.go()
        }
    }

    private fun go() {
        try {
            val config = configAndArguments.config
            if(config.getAsBoolean("256.color.demo", false)){
                printStream.println(Color256Demo().demoForConsole)
            } else if(config.getAsBoolean("16.color.demo", false)){
                printStream.println(Color16Demo().demoForConsole)
            } else if(config.getAsBoolean("man", false)){
                printStream.println(ManGenerator(DocWriterFactory.ConsoleText, config.getAsBoolean("debug", false)).man)
            } else if(config.getAsBoolean("help", false)){
                HelpGenerator().go(outputStream);
            } else if(config.getAsBoolean("install", false)){
                ExampleAppPropertiesFileCreator().createIfNecessary()
            } else if(config.getAsBoolean("piped.input", false)){
                readFromPipedInput()
            } else {
                println("Will look for file")
            }
        } finally {
            outputStream.flush()
            outputStream.close()
        }
    }

    private fun readFromPipedInput() {
        val reader = inputStream.bufferedReader()
        println("About to read from piped input")
        while (true) {
            val line = reader.readLine()
            println("After reading first line: $line")
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