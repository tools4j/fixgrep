package org.tools4j.fixgrep.orders

import org.tools4j.fixgrep.linehandlers.FixLine
import org.tools4j.fixgrep.linehandlers.FixLineHandler
import org.tools4j.fixgrep.formatting.Formatter
import java.util.function.Consumer

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:13 PM
 */
class OrderGroupingFixLineHandler(
        val formatter: Formatter,
        val uniqueIdSpecs: UniqueIdSpecs,
        val idFilter: IdFilter,
        val output: Consumer<String>) : FixLineHandler {

    constructor(formatter: Formatter,
                uniqueIdSpecs: UniqueIdSpecs,
                output: Consumer<String>): this(formatter, uniqueIdSpecs, IdFilter(), output)

    val groupedOrders = GroupedOrdersImpl(uniqueIdSpecs)

    override fun handle(fixLine: FixLine) {
        groupedOrders.handle(fixLine)
    }

    override fun finish() {
        OrderGroupingPrinter(groupedOrders, idFilter, formatter, output ).print()
    }
}