package org.tools4j.fix

import org.tools4j.model.ExecType
import org.tools4j.model.Id
import org.tools4j.model.OrdStatus
import org.tools4j.model.OrderType
import org.tools4j.model.Price
import org.tools4j.model.Side

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:39 PM
 */
class EnumValue (override val rawValue: String, private val enumDescription: String) : Value {

    override val valueWithAnnotatedPrefix: String
        get() = "[$enumDescription]$rawValue"

    override val valueWithAnnotatedPostfix: String
        get() = "$rawValue[$enumDescription]"

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
        if (other !is EnumValue) return false

        if (rawValue != other.rawValue) return false

        return true
    }

    override fun hashCode(): Int {
        return rawValue.hashCode()
    }
}
