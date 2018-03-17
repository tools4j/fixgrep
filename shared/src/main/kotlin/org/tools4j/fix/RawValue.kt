package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:39 PM
 */
class RawValue(override val rawValue: String) : Value {

    override val valueWithAnnotatedPrefix: String
        get() = throw UnsupportedOperationException("RawValue only. No specification data has been stored with this value of " + rawValue)

    override val valueWithAnnotatedPostfix: String
        get() = throw UnsupportedOperationException("RawValue only. No specification data has been stored with this value of " + rawValue)

    override fun intValue(): Int {
        throw UnsupportedOperationException()
    }

    override fun doubleValue(): Double {
        throw UnsupportedOperationException()
    }

    override fun longValue(): Long {
        throw UnsupportedOperationException()
    }

    override fun booleanValue(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun priceValue(): Price {
        throw UnsupportedOperationException()
    }

    override fun longValueFromUTCTimestamp(): Long {
        throw UnsupportedOperationException()
    }

    override fun sideValue(): Side {
        throw UnsupportedOperationException()
    }

    override fun orderTypeValue(): OrderType {
        throw UnsupportedOperationException()
    }

    override fun idValue(): Id {
        throw UnsupportedOperationException()
    }

    override fun execTypeValue(): ExecType {
        throw UnsupportedOperationException()
    }

    override fun ordStatusValue(): OrdStatus {
        throw UnsupportedOperationException()
    }

    override fun toString(): String {
        return valueWithAnnotatedPostfix
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RawValue) return false

        if (rawValue != other.rawValue) return false

        return true
    }

    override fun hashCode(): Int {
        return rawValue.hashCode()
    }
}
