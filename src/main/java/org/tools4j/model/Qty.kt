package org.tools4j.model

/**
 * User: ben
 * Date: 20/7/17
 * Time: 6:38 AM
 */
abstract class Qty(val value: Long) : Number() {

    override fun toLong(): Long {
        return value
    }

    override fun toInt(): Int {
        return toLong().toInt()
    }

    override fun toFloat(): Float {
        return toLong().toFloat()
    }

    override fun toDouble(): Double {
        return toLong().toDouble()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Qty) return false
        val qty = other as Qty?

        return toLong() == qty!!.toLong()
    }

    override fun hashCode(): Int {
        val longValue = toLong()
        return (longValue xor longValue.ushr(32)).toInt()
    }

    override fun toShort(): Short {
        throw UnsupportedOperationException()
    }

    override fun toByte(): Byte {
        throw UnsupportedOperationException()
    }

    override fun toChar(): Char {
        throw UnsupportedOperationException()
    }
}
