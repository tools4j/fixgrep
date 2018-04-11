package org.tools4j.fix

import java.util.*

/**
 * User: ben
 * Date: 19/10/2016
 * Time: 5:41 AM
 */
interface Price {
    val orNull: Double?
    val orderType: OrderType
    fun hasPrice(): Boolean
    fun get(): Double
    fun mid(other: Price): Price
    fun orElse(other: Double): Price
    fun orElse(other: Price): Price

    class PriceComparator(private val priceAsDoubleComparator: Comparator<Double>) : Comparator<Price> {
        override fun compare(q1: Price, q2: Price): Int {
            if (!q1.hasPrice() && !q2.hasPrice()) {
                return 0
            } else if (!q2.hasPrice()) {

            }
            return priceAsDoubleComparator.compare(q1.orNull, q2.orNull)
        }

        companion object {
            fun sortLowToHigh(): PriceComparator {
                return PriceComparator(PriceAsDoubleComparator.sortLowToHigh())
            }

            fun sortHighToLow(): PriceComparator {
                return PriceComparator(PriceAsDoubleComparator.sortLowToHigh())
            }
        }
    }

    class PriceAsDoubleComparator(private val comparatorMultiplier: Int) : Comparator<Double> {
        override fun compare(q1: Double?, q2: Double?): Int {
            if (q1 == null && q2 == null)
                return 0
            else if (q1 == null && q2 != null)
                return -1
            else if (q1 != null && q2 == null)
                return 1
            else {
                val c = java.lang.Double.compare(q1!!, q2!!)
                return comparatorMultiplier * c
            }
        }

        companion object {
            fun sortLowToHigh(): PriceAsDoubleComparator {
                return PriceAsDoubleComparator(1)
            }
            fun sortHighToLow(): PriceAsDoubleComparator {
                return PriceAsDoubleComparator(-1)
            }
        }
    }

    companion object {
        fun valueOf(str: String): Price {
            return if (str.equals(MarketPrice.MARKET_STR, ignoreCase = true)) {
                MarketPrice.INSTANCE
            } else {
                LimitPrice(java.lang.Double.valueOf(str))
            }
        }

        fun equals(one: Price?, two: Price?): Boolean {
            if(one == two) return true;
            if(one == null && two == null) return true
            if(one == null || two == null) return false
            return one.get() == two.get()
        }

        fun hashCode(price: Price): Int {
            return price.get().hashCode()
        }
    }
}
