package org.tools4j.fixgrep.main

import org.tools4j.fixgrep.formatting.FormatSpec
import org.tools4j.fixgrep.formatting.Formatter
import org.tools4j.fixgrep.linehandlers.DefaultFixLineHandler
import org.tools4j.fixgrep.linehandlers.DefaultTextLineHandler
import org.tools4j.fixgrep.linehandlers.FixLineHandler
import org.tools4j.fixgrep.linehandlers.LineHandler
import org.tools4j.fixgrep.orders.*
import java.util.function.Consumer

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 07:15
 */
class FormattingDi(val diContext: DiContext, val inputDi: InputDi, val outputDi: OutputDi) {
    init {
        diContext.addRunnable { this.start() }
    }

    val formatter: Formatter by lazy {
        Formatter(FormatSpec(diContext.config))
    }

    val fixLineHandler: FixLineHandler by lazy {
        if(diContext.config.groupByOrder){
            OrderGroupingFixLineHandler(
                    formatter,
                    UniqueIdSpecs(
                            UniqueClientOrderIdSpec(),
                            UniqueOriginalClientOrderIdSpec(),
                            UniqueOrderIdSpec()),
                    IdFilter(diContext.config.getIdsToOrdersGroupsBy),
                    Consumer {outputDi.printStream.print(it)})
        } else {
            DefaultFixLineHandler(formatter, Consumer { outputDi.printStream.print(it) })
        }
    }

    val textLineHandler: LineHandler by lazy {
        DefaultTextLineHandler(formatter.spec, fixLineHandler)
    }

    fun start(){
        val reader = inputDi.lineReader
        while (true) {
            val line = reader.readLine()
            if (line == null) break
            else textLineHandler.handle(line)
        }
        textLineHandler.finish()
    }
}