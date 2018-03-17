package org.tools4j.fix

import org.tools4j.utils.FormatUtils

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:38 PM
 */
class NonEnumValue(override val rawValue: String): Value {

    override val valueWithAnnotatedPrefix: String
        get() = rawValue

    override val valueWithAnnotatedPostfix: String
        get() = rawValue


    override fun toString(): String {
        return rawValue
    }

    override fun intValue(): Int {
        return Integer.parseInt(rawValue)
    }

    override fun doubleValue(): Double {
        return java.lang.Double.parseDouble(rawValue)

    }

    override fun longValue(): Long {
        return java.lang.Long.parseLong(rawValue)
    }

    override fun booleanValue(): Boolean {
        return java.lang.Boolean.parseBoolean(rawValue)
    }

    override fun priceValue(): Price {
        return Price.valueOf(rawValue)
    }

    override fun longValueFromUTCTimestamp(): Long {
        return FormatUtils.fromUTCTimestamp(rawValue)
    }

    override fun sideValue(): Side {
        return Side.forCode(intValue())
    }

    override fun orderTypeValue(): OrderType {
        return OrderType.forCode(intValue())
    }

    override fun idValue(): Id {
        return SimpleId(toString())
    }

    override fun execTypeValue(): ExecType {
        return ExecType.forCode(rawValue)
    }

    override fun ordStatusValue(): OrdStatus {
        return OrdStatus.forCode(rawValue)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NonEnumValue) return false

        if (rawValue != other.rawValue) return false

        return true
    }

    override fun hashCode(): Int {
        return rawValue.hashCode()
    }
}
