package org.tools4j.fix

/**
 * User: ben
 * Date: 19/10/2016
 * Time: 5:43 AM
 */
abstract class BaseLimitPrice : Price {

    override val orNull: Double?
        get() = get()

    override fun hasPrice(): Boolean {
        return true
    }

    override fun mid(other: Price): Price {
        return if (this.hasPrice() && other.hasPrice()) {
            LimitPriceWithRoundedFloatingPointError(LimitPrice((get() + other.get()) / 2))
        } else {
            MarketPrice.INSTANCE
        }
    }

    override fun toString(): String {
        return "" + get()
    }


    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        return if (!(o as Price).hasPrice()) false else java.lang.Double.compare(o.get(), get()) == 0
    }

    override fun hashCode(): Int {
        val temp = java.lang.Double.doubleToLongBits(get())
        return (temp xor temp.ushr(32)).toInt()
    }
}
