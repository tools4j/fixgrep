package org.tools4j.fixgrep.main

import mu.KLogging
import org.tools4j.fixgrep.formatting.FormatSpec
import org.tools4j.fixgrep.formatting.Formatter
import org.tools4j.fixgrep.linehandlers.DefaultFixLineHandler
import org.tools4j.fixgrep.linehandlers.DefaultTextLineHandler
import org.tools4j.fixgrep.linehandlers.FixLineHandler
import org.tools4j.fixgrep.linehandlers.LineHandler
import org.tools4j.fixgrep.main.FixGrep.Companion.logger
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

    companion object: KLogging()

    val formatter: Formatter by lazy {
        Formatter(FormatSpec(diContext.config))
    }

    val consumerToPrintln: Consumer<String> by lazy {
        Consumer<String> {
            logger.debug { "About to send string to output" }
            outputDi.printStream.print(it)
            logger.debug { "Finished sending string to output" }
        }
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
                    consumerToPrintln)
        } else {
            DefaultFixLineHandler(formatter, consumerToPrintln)
        }
    }

    val textLineHandler: LineHandler by lazy {
        DefaultTextLineHandler(formatter.spec, fixLineHandler)
    }

    fun start(){
        val reader = inputDi.lineReader
        logger.debug { "Starting to read lines" }
        while (true) {
            val line = reader.readLine()
            if (line == null) break
            else textLineHandler.handle(line)
        }
        logger.debug { "Finished reading lines" }
        textLineHandler.finish()
    }
}