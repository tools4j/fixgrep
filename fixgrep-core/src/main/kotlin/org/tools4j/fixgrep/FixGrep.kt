package org.tools4j.fixgrep

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 7:00 AM
 */
class FixGrep(val inputStream: InputStream, val outputStream: OutputStream, val config: Config) {
    val formatter: Formatter by lazy {
        Formatter(
            logLineRegex = config.getString("line.regex"),
            logLineRegexGroupContainingMessage = config.getString("line.regexgroup").toInt(),
            format = config.getString("format"),
            inputFixDelimiter = config.getString("input.delimiter").toCharArray()[0],
            outputFixDelimiter = config.getString("output.delimiter").toCharArray()[0])
    }

    val printStream: PrintStream by lazy {
        PrintStream(outputStream)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val fixGrep = FixGrep(System.`in`, System.out, ConfigFactory.load())
            fixGrep.go()
        }
    }

    private fun go() {
        inputStream.bufferedReader().useLines { lines -> lines.forEach { handleLine(it) } }
    }

    private fun handleLine(line: String) {
        val matcher = formatter.matches(line)
        if (matcher.find()) {
            printStream.println(formatter.format(matcher))
        }
    }
}