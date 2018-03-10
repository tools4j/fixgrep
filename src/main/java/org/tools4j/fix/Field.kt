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
class Field(val tag: Tag, val value: Value) {

    val withOutsideAnnotations: String
        get() = tag.tagWithAnnotatedPrefix + "=" + value.valueWithAnnotatedPostfix

    val withInsideAnnotations: String
        get() = tag.tagWithAnnotatedPostfix + "=" + value.valueWithAnnotatedPrefix

    val withPrefixAnnotations: String
        get() = tag.tagWithAnnotatedPrefix + "=" + value.valueWithAnnotatedPrefix

    val withPostfixAnnotations: String
        get() = tag.tagWithAnnotatedPostfix + "=" + value.valueWithAnnotatedPostfix

    val withNoAnnotations: String
        get() = tag.tag.toString() + "=" + value.rawValue

    constructor(tag: Number, value: String) : this(UnknownTag(tag), NonEnumValue(value)) {}

    constructor(tag: String, value: String) : this(Integer.valueOf(tag), value) {}

    override fun toString(): String {
        return tag.toString() + "=" + value
    }

    fun intValue(): Int {
        try {
            return value.intValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    fun stringValue(): String {
        try {
            return value.toString()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }


    fun doubleValue(): Double {
        try {
            return value.doubleValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    fun longValue(): Long {
        try {
            return value.longValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    fun booleanValue(): Boolean {
        try {
            return value.booleanValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    fun priceValue(): Price {
        try {
            return value.priceValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    fun longValueFromUTCTimestamp(): Long {
        try {
            return value.longValueFromUTCTimestamp()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    fun sideValue(): Side {
        try {
            return value.sideValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    fun orderTypeValue(): OrderType {
        try {
            return value.orderTypeValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    fun idValue(): Id {
        try {
            return value.idValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    fun execTypeValue(): ExecType {
        try {
            return value.execTypeValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    fun ordStatusValue(): OrdStatus {
        try {
            return value.ordStatusValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Field) return false

        if (tag != other.tag) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tag.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}
