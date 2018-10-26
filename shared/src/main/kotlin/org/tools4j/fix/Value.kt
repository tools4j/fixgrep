package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:27 PM
 */
interface Value {
    val valueRaw: String
    fun intValue(): Int
    fun doubleValue(): Double
    fun longValue(): Long
    fun booleanValue(): Boolean
    fun priceValue(): Price
    fun longValueFromUTCTimestamp(): Long
    fun sideValue(): Side
    fun orderTypeValue(): OrderType
    fun idValue(): Id
    fun execTypeValue(): ExecType
    fun ordStatusValue(): OrdStatus
    fun accept(valueVisitor: ValueVisitor)
    fun isEmpty(): Boolean {
        return toString().isEmpty()
    }
}
