package org.tools4j.fix

/**
 * User: ben
 * Date: 19/10/2016
 * Time: 5:42 AM
 */
class MarketPrice private constructor() : Price {

    override val orNull: Double?
        get() = null

    override fun hasPrice(): Boolean {
        return false
    }

    override fun get(): Double {
        throw UnsupportedOperationException()
    }

    override fun orElse(other: Double): Price {
        return LimitPrice(other)
    }

    override fun orElse(other: Price): Price {
        return other
    }

    override fun mid(other: Price): Price {
        return other
    }


    override fun toString(): String {
        return MARKET_STR
    }

    override fun hashCode(): Int {
        return 1
    }

    override fun equals(o: Any?): Boolean {
        return !(o as Price).hasPrice()
    }

    companion object {
        val INSTANCE = MarketPrice()
        val MARKET_STR = "MARKET"
    }
}
