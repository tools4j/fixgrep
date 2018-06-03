package org.tools4j.fixgrep

import org.tools4j.fixgrep.help.Color16Demo
import org.tools4j.fixgrep.help.Color256Demo
import org.tools4j.fixgrep.help.DocWriterFactory
import org.tools4j.fixgrep.help.ExampleAppPropertiesFileCreator
import org.tools4j.fixgrep.help.ManGenerator
import org.tools4j.properties.Config
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream


/**
 * User: ben
 * Date: 12/03/2018
 * Time: 7:00 AM
 */
class FixGrep(val inputStream: InputStream, val outputStream: OutputStream, val config: Config) {

    constructor(inputStream: InputStream, outputStream: OutputStream, args: Array<String>)
            : this(inputStream, outputStream, ConfigBuilder(args).config)

    val formatter: Formatter by lazy {
        Formatter(FormatSpec(config))
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
            if (!config.getAsBoolean("skip.app.properties.creation")) {
                ExampleAppPropertiesFileCreator().createIfNecessary()
            }
            if(config.getAsBoolean("256.color.demo", false)){
                printStream.println(Color256Demo().demoForConsole)
            } else if(config.getAsBoolean("16.color.demo", false)){
                printStream.println(Color16Demo().demoForConsole)
            } else if(config.getAsBoolean("man", false)){
                printStream.println(ManGenerator(DocWriterFactory.Html).man)
            } else {
                goFixGrep()
            }
        } finally {
            outputStream.flush()
            outputStream.close()
        }
    }

    private fun goFixGrep() {
        val reader = inputStream.bufferedReader()
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