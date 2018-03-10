package org.tools4j.model

/**
 * User: ben
 * Date: 10/01/2017
 * Time: 6:15 PM
 */
class LimitPriceWithRoundedFloatingPointError(private val unRoundedPrice: Price) : Price {

    override val orNull: Double?
        get() = roundedPrice.get()

    val roundedPrice: Price by lazy {
        if (unRoundedPrice.hasPrice()) {
            LimitPrice(round(unRoundedPrice.get()))
        } else {
            MarketPrice.INSTANCE
        }
    }

    override fun hasPrice(): Boolean {
        return false
    }

    override fun get(): Double {
        return roundedPrice.get()
    }

    override fun mid(other: Price): Price {
        return LimitPriceWithRoundedFloatingPointError(roundedPrice.mid(other))
    }

    override fun orElse(other: Double): Price {
        return roundedPrice.orElse(other)
    }

    override fun orElse(other: Price): Price {
        return roundedPrice.orElse(other)
    }

    private fun round(price: Double): Double {
        return Math.floor(price * EPSILON_MULTIPLIER) / EPSILON_MULTIPLIER
    }

    override fun toString(): String {
        return "" + get()
    }

    override fun equals(other: Any?): Boolean {
        return if(other !is Price) return false
        else Price.equals(roundedPrice, other);
    }

    override fun hashCode(): Int {
        return Price.hashCode(roundedPrice)
    }

    companion object {
        private val EPSILON_MULTIPLIER = 10_000_000_000L
    }
}
