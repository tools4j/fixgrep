package org.tools4j.model

/**
 * User: ben
 * Date: 27/10/2016
 * Time: 5:30 PM
 */
interface PriceOperators {

    fun equalToOrMoreAggressiveThan(price: Double?): Boolean

    fun moreAggressiveThan(price: Double?): Boolean

    fun equalToOrMorePassiveThan(price: Double?): Boolean

    fun morePassiveThan(price: Double?): Boolean

    fun equalToOrMoreAggressiveThan(price: Price): Boolean

    fun moreAggressiveThan(price: Price): Boolean

    fun equalToOrMorePassiveThan(price: Price): Boolean

    fun morePassiveThan(price: Price): Boolean
}