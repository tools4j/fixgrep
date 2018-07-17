package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:39 PM
 */
open class RawValue(override val valueRaw: String) : Value {
    override fun accept(valueVisitor: ValueVisitor) {
        valueVisitor.visit(this)
    }

    override fun toString(): String {
        return valueRaw
    }

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RawValue) return false

        if (valueRaw != other.valueRaw) return false

        return true
    }

    override fun hashCode(): Int {
        return valueRaw.hashCode()
    }
}
