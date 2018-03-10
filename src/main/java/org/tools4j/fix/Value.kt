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
 * Time: 5:27 PM
 */
interface Value {
    val rawValue: String
    val valueWithAnnotatedPrefix: String
    val valueWithAnnotatedPostfix: String
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
}
