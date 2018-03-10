package org.tools4j.model


/**
 * User: ben
 * Date: 24/10/2016
 * Time: 5:48 PM
 */
class PriceOperations(val side: Side, val priceAsDoubleBookComparator: Price.PriceAsDoubleComparator) {
    val priceableBookComparator: Priceable.PriceableComparator
    val priceBookComparator: Price.PriceComparator

    init {
        this.priceBookComparator = Price.PriceComparator(priceAsDoubleBookComparator)
        this.priceableBookComparator = Priceable.PriceableComparator(priceBookComparator)
    }

    fun isPrice(price: Double): Operator {
        return Operator(side, price)
    }

    fun isPrice(price: Price): Operator {
        return Operator(side, price.orNull)
    }

    override fun toString(): String {
        return "PriceOperations{" +
                "side=" + side +
                '}'.toString()
    }


    class Operator(private val side: Side, private val price: Double?) : PriceOperators {

        constructor(side: Side, price: Price) : this(side, if(price.hasPrice()) price.get() else null)

        /*
        The priceAsDoubleBookComparator will sort orders from passive -> aggressive
        This means that:
        compare(aggressive, passive) == 1
        compare(passive, aggressive) == -1
         */

        override fun equalToOrMoreAggressiveThan(price: Double?): Boolean {
            return side.priceOperations.priceAsDoubleBookComparator.compare(this.price, price) <= 0
        }

        override fun moreAggressiveThan(price: Double?): Boolean {
            return side.priceOperations.priceAsDoubleBookComparator.compare(this.price, price) < 0
        }

        override fun equalToOrMorePassiveThan(price: Double?): Boolean {
            return side.priceOperations.priceAsDoubleBookComparator.compare(this.price, price) >= 0
        }

        override fun morePassiveThan(price: Double?): Boolean {
            return side.priceOperations.priceAsDoubleBookComparator.compare(this.price, price) > 0
        }

        override fun equalToOrMoreAggressiveThan(price: Price): Boolean {
            return equalToOrMoreAggressiveThan(price.orNull)
        }

        override fun moreAggressiveThan(price: Price): Boolean {
            return moreAggressiveThan(price.orNull)
        }

        override fun equalToOrMorePassiveThan(price: Price): Boolean {
            return equalToOrMorePassiveThan(price.orNull)
        }

        override fun morePassiveThan(price: Price): Boolean {
            return morePassiveThan(price.orNull)
        }

        override fun toString(): String {
            return "Operator{" +
                    "side=" + side +
                    ", price=" + price +
                    '}'.toString()
        }
    }
}
