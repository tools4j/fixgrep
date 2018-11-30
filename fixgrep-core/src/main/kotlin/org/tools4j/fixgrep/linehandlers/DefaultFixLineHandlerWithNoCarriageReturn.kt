package org.tools4j.fixgrep.linehandlers

import org.tools4j.fixgrep.formatting.Formatter
import java.util.function.Consumer

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:13 PM
 */
class DefaultFixLineHandlerWithNoCarriageReturn(val formatter: Formatter, val lineWriter: Consumer<String>) : FixLineHandler {
    override fun handle(fixLine: FixLine) {
        if(!formatter.spec.shouldPrint(fixLine)) return
        val formattedLine = formatter.format(fixLine)
        if(formattedLine != null) lineWriter.accept(formattedLine)
    }

    override fun finish() {
        //no-op
    }
}