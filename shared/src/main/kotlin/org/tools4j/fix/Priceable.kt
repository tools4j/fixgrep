package org.tools4j.fix

import java.util.Comparator

/**
 * User: ben
 * Date: 19/10/2016
 * Time: 5:50 AM
 */
interface Priceable {
    val price: Price

    class PriceableComparator(private val priceComparator: Comparator<Price>) : Comparator<Priceable> {
        override fun compare(q1: Priceable?, q2: Priceable?): Int {
            return if (q1 == null && q2 == null)
                0
            else if (q1 == null || q2 == null)
                1
            else
                priceComparator.compare(q1.price, q2.price)
        }

        companion object {
            fun sortLowToHigh(): PriceableComparator {
                return PriceableComparator(Price.PriceComparator.sortLowToHigh())
            }

            fun sortHighToLow(): PriceableComparator {
                return PriceableComparator(Price.PriceComparator.sortHighToLow())
            }
        }
    }
}