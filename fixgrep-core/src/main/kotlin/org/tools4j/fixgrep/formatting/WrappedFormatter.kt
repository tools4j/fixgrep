package org.tools4j.fixgrep.formatting

import org.tools4j.fixgrep.linehandlers.DefaultFixLineHandlerWithNoCarriageReturn
import org.tools4j.fixgrep.linehandlers.DefaultTextLineHandler
import java.util.function.Consumer

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:13 PM
 */
class WrappedFormatter(val spec: FormatSpec){
    var result: String? = null
    val formatter = Formatter(spec)
    val fixLineHandler = DefaultFixLineHandlerWithNoCarriageReturn(formatter, Consumer { result = it })
    val lineHandler = DefaultTextLineHandler(spec, fixLineHandler)

    fun format(line: String): String? {
        lineHandler.handle(line)
        val result = this.result
        this.result = null
        return result
    }
}