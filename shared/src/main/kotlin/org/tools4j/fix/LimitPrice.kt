package org.tools4j.fix

/**
 * User: ben
 * Date: 19/10/2016
 * Time: 5:43 AM
 */
class LimitPrice(private val price: Double) : BaseLimitPrice() {

    override fun get(): Double {
        return price
    }

    override fun orElse(other: Double): Price {
        return this
    }

    override fun orElse(other: Price): Price {
        return this
    }
}
