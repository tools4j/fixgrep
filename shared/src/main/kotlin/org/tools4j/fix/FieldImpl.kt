package org.tools4j.fix


/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:27 PM
 */
open class FieldImpl(override val tag: Tag, override val value: Value) : Field {
    constructor(tag: Int, value: String) : this(RawTag(tag), RawValue(value)) {}

    constructor(tag: String, value: String) : this(Integer.valueOf(tag), value) {}

    override fun accept(fieldVisitor: FieldVisitor) {
        fieldVisitor.visit(this)
    }

    override fun toString(): String {
        return tag.toString() + "=" + value
    }

    override fun intValue(): Int {
        try {
            return value.intValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun stringValue(): String {
        try {
            return value.toString()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun doubleValue(): Double {
        try {
            return value.doubleValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun longValue(): Long {
        try {
            return value.longValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun booleanValue(): Boolean {
        try {
            return value.booleanValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun priceValue(): Price {
        try {
            return value.priceValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun longValueFromUTCTimestamp(): Long {
        try {
            return value.longValueFromUTCTimestamp()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun sideValue(): Side {
        try {
            return value.sideValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun orderTypeValue(): OrderType {
        try {
            return value.orderTypeValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun idValue(): Id {
        try {
            return value.idValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun execTypeValue(): ExecType {
        try {
            return value.execTypeValue()
        } catch (e: Exception) {
            throw RuntimeException("Cannot convert value of field $value with tag $tag", e)
        }

    }

    override fun ordStatusValue(): OrdStatus {
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
