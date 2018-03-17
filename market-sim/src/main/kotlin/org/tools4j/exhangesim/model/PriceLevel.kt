package org.tools4j.exhangesim.model

import org.tools4j.model.*
import org.tools4j.extensions.sumByLong
import org.tools4j.fix.Price
import org.tools4j.fix.PriceOperations
import org.tools4j.fix.PriceOperators
import org.tools4j.fix.Priceable
import org.tools4j.fix.Side
import java.util.ArrayList
import java.util.Comparator

/**
 * User: ben
 * Date: 20/10/2016
 * Time: 5:15 PM
 */
class PriceLevel(
        override val price: Price,
        val side: Side,
        val orders: ArrayList<MarketOrder> = ArrayList())
            : Priceable, MutableCollection<MarketOrder> by orders, PriceOperators by PriceOperations.Operator(side, price)  {

    fun isMarketPrice(): Boolean = !this.price.hasPrice()

    val leavesQty: Long
        get() = orders.sumByLong {it.leavesQty.toLong()}


    fun removeTerminal() {
        orders.removeAll(orders.filter{it.isTerminal()}.toList())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PriceLevel) return false

        val that = other as PriceLevel?

        return price.equals(that!!.price)
    }

    override fun hashCode(): Int {
        return price.hashCode()
    }

    fun isPrice() : PriceOperations.Operator{
        return side.priceOperations.isPrice(this.price)
    }

    fun addAllOrdersFrom(other: PriceLevel) {
        this.orders.addAll(other.orders)
    }

    fun isPriceGreaterThanOrEqualTo(price: Double): Boolean {
        return this.price.hasPrice() && this.price.get() >= price
    }

    fun isPriceLessThanOrEqualTo(price: Double): Boolean {
        return this.price.hasPrice() && this.price.get() <= price
    }

    class PriceLevelComparator(private val priceComparator: Price.PriceComparator) : Comparator<PriceLevel> {
        override fun compare(o1: PriceLevel, o2: PriceLevel): Int {
            return priceComparator.compare(o1.price, o2.price)
        }
    }
}

