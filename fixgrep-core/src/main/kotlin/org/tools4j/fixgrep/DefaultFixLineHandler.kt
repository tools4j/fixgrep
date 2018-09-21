package org.tools4j.fixgrep

import java.util.function.Consumer

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:13 PM
 */
class DefaultFixLineHandler(val formatter: Formatter, val output: Consumer<String>) : FixLineHandler {
    override fun handle(fixLine: FixLine) {
        if(!shouldPrint(fixLine)) return
        val formattedLine = formatter.format(fixLine)
        if(formattedLine != null) output.accept(formattedLine)
    }

    override fun finish() {
        //no-op
    }

    private fun shouldPrint(fixLine: FixLine): Boolean{
        if(!formatter.spec.includeOnlyMessagesOfType.isEmpty()
                && !formatter.spec.includeOnlyMessagesOfType.contains(fixLine.fields.msgTypeCode)){
            return false
        } else if(!formatter.spec.excludeMessagesOfType.isEmpty()
                && formatter.spec.excludeMessagesOfType.contains(fixLine.fields.msgTypeCode)) {
            return false
        } else {
            return true
        }
    }
}