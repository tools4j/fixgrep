package org.tools4j.fixgrep

import java.util.function.Consumer

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:13 PM
 */
class DefaultFixLineHandler(val formatter: Formatter, val output: Consumer<String>) : FixLineHandler {
    override fun handle(fixLine: FixLine) {
        if(!formatter.spec.shouldPrint(fixLine)) return
        val formattedLine = formatter.format(fixLine)
        if(formattedLine != null) output.accept(formattedLine + "\n")
    }

    override fun finish() {
        //no-op
    }
}