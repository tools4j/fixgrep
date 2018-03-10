package org.tools4j.exhangesim;

import org.tools4j.exhangesim.model.OrderBook
import org.tools4j.model.MarketOrder
import org.tools4j.model.VersionedOrder


/**
 * User: ben
 * Date: 20/7/17
 * Time: 7:25 PM
 *
 * A MarketFacingStrategy has the option of creating one to many
 * new market orders.
 *
 */
class Exchange(possibleInstruments: List<String> = arrayListOf("AUD/USD", "GBP/AUD", "GBP/USD")){
    private val orderBooksByInstrument: MutableMap<String, OrderBook> = HashMap()

    init {
        possibleInstruments.forEach { orderBooksByInstrument[it] = OrderBook(it)}
    }

    fun add(order: MarketOrder) {
        val orderBook = orderBooksByInstrument[order.instrument]
        orderBook!!.add(order)
        orderBook.removeTerminalOrders()
        orderBook.match();
    }
}
