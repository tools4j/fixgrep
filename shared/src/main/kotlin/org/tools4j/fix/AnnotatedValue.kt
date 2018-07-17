package org.tools4j.fix


/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:39 PM
 */
class AnnotatedValue(override val valueRaw: String, val annotation: String) : Value {
    override fun accept(valueVisitor: ValueVisitor) {
        valueVisitor.visit(this)
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
        if (other !is AnnotatedValue) return false

        if (valueRaw != other.valueRaw) return false
        if (annotation != other.annotation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = valueRaw.hashCode()
        result = 31 * result + annotation.hashCode()
        return result
    }
}
