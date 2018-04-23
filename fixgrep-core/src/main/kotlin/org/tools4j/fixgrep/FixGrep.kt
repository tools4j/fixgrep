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

    constructor(inputStream: InputStream, outputStream: OutputStream, args: Array<String>)
            : this(inputStream, outputStream, ConfigBuilder(args).config)

    val formatter: Formatter by lazy {
        Formatter(FormatSpec(config = config))
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
        val reader = inputStream.bufferedReader()
        while(true){
            val line = reader.readLine()
            if(line == null) break
            else handleLine(line)
        }
        outputStream.flush()
        outputStream.close()
    }

    private fun handleLine(line: String) {
        val matcher = formatter.shouldParse(line)
        if (matcher.find()) {
            val formattedLine = formatter.format(matcher)
            if(formattedLine != null){
                printStream.println(formattedLine)
            }
        }
    }
}