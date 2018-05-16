package org.tools4j.fix

/**
 * User: ben
 * Date: 4/04/2018
 * Time: 6:01 PM
 */
interface Field {
    val tag: Tag
    val value: Value
    override fun toString(): String
    fun intValue(): Int
    fun stringValue(): String
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
    fun toConsoleText(): String
    fun toHtml(): String
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}