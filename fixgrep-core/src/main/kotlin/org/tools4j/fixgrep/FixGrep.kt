package org.tools4j.fixgrep

import com.typesafe.config.Config
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
            logLineRegex = config.getString("fixgrep.log.line.regex"),
            logLineRegexGroupContainingMessage = config.getString("fixgrep.log.line.regexgroup.for.fix.msg").toInt(),
            format = config.getString("fixgrep.format"),
            inputFixDelimiter = config.getString("fixgrep.input.fix.delimiter").toCharArray()[0],
            outputFixDelimiter = config.getString("fixgrep.output.fix.delimiter").toCharArray()[0])
    }

    val printStream: PrintStream by lazy {
        PrintStream(outputStream)
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