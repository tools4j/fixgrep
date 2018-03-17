package org.tools4j.exhangesim.model

import org.tools4j.extensions.sumByLong
import org.tools4j.model.MarketOrder
import org.tools4j.fix.MarketPrice
import org.tools4j.fix.Price
import org.tools4j.fix.Side
import org.tools4j.model.fix.messages.OrderRequestMessage
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ConcurrentSkipListMap

/**
 * User: ben
 * Date: 1/8/17
 * Time: 6:44 AM
 */
class OrderBookSide(private val side: Side) {
    private val priceLevels: ConcurrentMap<Price, PriceLevel>

    val volumeAtMarketPrice: Long
        get() = priceLevels.values.filter { it.isMarketPrice() }.sumByLong { it.leavesQty }

    val orders: List<MarketOrder>
        get() = priceLevels.values.flatMap { it.orders }

    private val firstOrderWithLimitPrice: Price
        get() = orders.firstOrNull{ it.price.hasPrice() }?.price ?: MarketPrice.INSTANCE

    init {
        priceLevels = ConcurrentSkipListMap<Price, PriceLevel>(side.priceOperations.priceBookComparator)
    }

    fun add(order: MarketOrder) {
        val price = order.price
        var priceLevel: PriceLevel? = priceLevels[price]
        if (priceLevel == null) {
            priceLevel = PriceLevel(price, side)
            val racedPriceLevel = priceLevels.putIfAbsent(price, priceLevel)
            if (racedPriceLevel != null) {
                priceLevel = racedPriceLevel
            }
        }
        priceLevel.add(order)
    }

    fun removeTerminal() {
        priceLevels.values.forEach{it.removeTerminal()}
        priceLevels.filter { it.value.isEmpty() }.forEach{ priceLevels.remove(it.key) }
    }

    fun uniquePrices(): List<Double> {
        return priceLevels.values.filter { !it.isMarketPrice() }.map { it.price.get() }.distinct()
    }

    fun getVolumeGreaterThanOrEqualTo(price: Double): Long {
        return priceLevels.values.filter { it.isPriceGreaterThanOrEqualTo(price) }.sumByLong { it.leavesQty }
    }

    fun getVolumeLessThanOrEqualTo(price: Double): Long {
        return priceLevels.values.filter { it.isPriceLessThanOrEqualTo(price) }.sumByLong { it.leavesQty }
    }

    fun getLeavesQtyEqualToOrMoreAggressiveThan(price: Double): Long {
        return priceLevels.values.filter { it.equalToOrMoreAggressiveThan(price) }.sumByLong { it.leavesQty }
    }

    fun getMidWith(other: OrderBookSide): Price {
        val thisTop = firstOrderWithLimitPrice
        val otherTop = other.firstOrderWithLimitPrice
        return if (!thisTop.hasPrice() && !otherTop.hasPrice()) {
            MarketPrice.INSTANCE
        } else if (!otherTop.hasPrice()) {
            thisTop
        } else if (!thisTop.hasPrice()) {
            otherTop
        } else {
            thisTop.mid(otherTop)
        }
    }

    fun getOrder(orderRequestMessage: OrderRequestMessage): MarketOrder? {
        return orders.firstOrNull { it.origClOrdId == orderRequestMessage.origClOrderId }
    }
}

