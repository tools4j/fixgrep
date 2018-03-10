package org.tools4j.exhangesim.model

import mu.KLogging
import org.tools4j.model.DateTimeService
import org.tools4j.model.IdGenerator
import org.tools4j.model.MarketOrder
import org.tools4j.model.MarketPrice
import org.tools4j.model.Price
import org.tools4j.model.Side
import org.tools4j.model.VersionedOrder
import org.tools4j.model.fix.messages.OrderRequestMessage
import java.text.NumberFormat
import java.util.*

/**
 * User: ben
 * Date: 15/04/2016
 * Time: 5:16 PM
 */
class OrderBook @JvmOverloads constructor(
        private val symbol: String,
        private val lastTradedPrice: Price = MarketPrice.INSTANCE){

    companion object: KLogging()
    private val bids: OrderBookSide
    private val asks: OrderBookSide
    private val listeners = ArrayList<BookChangeListener>()
    private val visitors = ArrayList<BookChangeVisitor>()

    val allPrices: Collection<Double>
        get() {
            val prices = TreeSet<Double>()
            prices.addAll(bids.uniquePrices())
            prices.addAll(asks.uniquePrices())
            return prices
        }

    val mid: Price
        get() = bids.getMidWith(asks)

    init {
        bids = OrderBookSide(Side.BID)
        asks = OrderBookSide(Side.ASK)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        val bidIt = bids.orders.iterator()
        val askIt = asks.orders.iterator()
        sb.append("                           BUY | SELL")
        while (bidIt.hasNext() || askIt.hasNext()) {
            sb.append("\n")
            if (bidIt.hasNext()) {
                val order = bidIt.next()
                sb.append(String.format("%30s", order.toStringWithAge()))
            } else {
                //When no more bids exist, we still need to fix in the space
                sb.append(String.format("%30s", ""))
            }
            sb.append(" | ")
            if (askIt.hasNext()) {
                val order = askIt.next()
                sb.append(String.format("%-30s", order.toStringWithAge()))
            }
        }
        return sb.toString()
    }

    fun add(order: MarketOrder) {
        if (order.instrument != symbol) {
            throw IllegalArgumentException("Order instrument " + order.instrument + "must match book instrument " + symbol)
        }
        getBookSide(order.side).add(order)
        notifyVisitors()
        notifyListeners()
    }

    private fun getBookSide(side: Side): OrderBookSide {
        return if (side.isBuy) bids else asks
    }

    private fun notifyListeners() {
        listeners.forEach { listener -> listener.onChange(this) }
    }

    private fun notifyVisitors() {
        visitors.forEach { visitor -> visitor.visit(this) }
    }

    fun addListener(listener: BookChangeListener) {
        this.listeners.add(listener)
    }

    fun getBidLeavesQtyEqualToOrMoreAggressiveThan(price: Double): Long {
        return bids.getLeavesQtyEqualToOrMoreAggressiveThan(price)
    }

    fun getAskLeavesQtyEqualToOrMoreAggressiveThan(price: Double): Long {
        return asks.getLeavesQtyEqualToOrMoreAggressiveThan(price)
    }

    fun getBids(): Iterator<MarketOrder> {
        return getBookSide(Side.BID).orders.iterator()
    }

    fun getAsks(): Iterator<MarketOrder> {
        return getBookSide(Side.ASK).orders.iterator()
    }

    fun getLeavesQtyEqualToOrMoreAggressiveThan(side: Side, price: Double): Long {
        return getBookSide(side).getLeavesQtyEqualToOrMoreAggressiveThan(price)
    }

    fun removeTerminalOrders() {
        bids.removeTerminal()
        asks.removeTerminal()
    }

    fun match() {
        try {
            val bids = getBids()
            var asks = getAsks()

            if (!bids.hasNext() || !asks.hasNext()) {
                return
            }

            logger.debug { "===========================================" }
            logger.debug { "===========================================" }
            logger.debug { "START OF MATCHING: " + this.symbol}
            logger.debug { "===========================================" }
            logger.debug { "===========================================" }

            val matches = ArrayList<Match>()
            while (bids.hasNext()) {
                logger.debug { "Book showing remaining quantities:\n" + toString() }
                if(!asks.hasNext()){
                    return
                }
                val bid = bids.next()
                logger.debug { "===========================================" }
                logger.debug { "Considering bid: ${bid.qtyAtPrice}" }
                logger.debug { "===========================================" }
                if (bid.leavesQty <= 0) {
                    logger.debug { "Skipping bid, no qty remaining" }
                    continue
                }
                asks = getAsks()
                while (asks.hasNext()) {
                    logger.debug { ":::Matching state::::\n" }
                    logger.debug { "Book:\n" + toString() }
                    logger.debug { "Matches:\n" + MatchResults(matches).toPrettyString() }
                    val ask = asks.next()
                    logger.debug { "Attempting to match bid and ask: " + bid.qtyAtPrice + " " + ask.qtyAtPrice }
                    if (ask.leavesQty <= 0) {
                        logger.debug { "Skipping ask, no qty remaining" }
                        continue
                    }
                    if (bid.crossesWith(ask)) {
                        val bidLeavesQty = bid.leavesQty
                        val askLeavesQty = ask.leavesQty
                        val execQty = Math.min(bidLeavesQty, askLeavesQty)
                        logger.debug { "Bid & ask crossing[" + NumberFormat.getIntegerInstance().format(execQty) + "@" + ask.price + "]: bid:" + bid + " ask:" + ask }
                        val bidAskMidPrice = bid.getMidWith(ask)
                        val midPrice: Price
                        if (bidAskMidPrice.hasPrice()) {
                            midPrice = bidAskMidPrice
                        } else {
                            midPrice = mid.orElse(lastTradedPrice)
                        }
                        matches.add(Match(bid, ask, execQty, midPrice))

                        bid.fill(execQty, midPrice)
                        if(bid.leavesQty != (bidLeavesQty - execQty)){
                            throw RuntimeException("Quantity was not removed from order!  Probably a routing problem, i.e. the execution report is not being recieved by the order.")
                        }

                        ask.fill(execQty, midPrice)
                        if(ask.leavesQty != (askLeavesQty - execQty)){
                            throw RuntimeException("Quantity was not removed from order!  Probably a routing problem, i.e. the execution report is not being recieved by the order.")
                        }

                        removeTerminalOrders()
                        if (bid.leavesQty <= 0) {
                            break
                        }
                    } else {
                        logger.debug { "Bid & ask NOT crossing any more: ${bid.qtyAtPrice} ${ask.qtyAtPrice}, time to finish matching" }
                    }
                }
            }
        } finally {
            logger.debug{"Book after matching:\n" + toString()}
        }
    }

    fun getOrder(orderRequestMessage: OrderRequestMessage): MarketOrder? {
        return if (orderRequestMessage.side.isBuy) {
            bids.getOrder(orderRequestMessage)
        } else {
            asks.getOrder(orderRequestMessage)
        }
    }
}
