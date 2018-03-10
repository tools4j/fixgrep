package org.tools4j.model

import java.util.Date
import java.util.Random

/**
 * User: ben
 * Date: 20/7/17
 * Time: 7:28 PM
 */
class RandomLongGivenAverage(private val average: Long) : Number() {
    private val random = Random(Date().time)

    override fun toInt(): Int {
        return toLong().toInt()
    }

    override fun toLong(): Long {
        return (2 * average * random.nextDouble()).toLong()
    }

    override fun toFloat(): Float {
        return toLong().toFloat()
    }

    override fun toDouble(): Double {
        return toLong().toDouble()
    }

    override fun toByte(): Byte {
        throw UnsupportedOperationException()
    }

    override fun toChar(): Char {
        throw UnsupportedOperationException()
    }

    override fun toShort(): Short {
        throw UnsupportedOperationException()
    }
}
