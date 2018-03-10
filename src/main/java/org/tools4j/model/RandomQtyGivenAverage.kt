package org.tools4j.model

import java.util.Date
import java.util.Random

/**
 * User: ben
 * Date: 19/7/17
 * Time: 5:47 PM
 */
class RandomQtyGivenAverage(private val average: Long) : Qty(0) {
    private val random = Random(Date().time)

    override fun toLong(): Long {
        return (average.toDouble() * 2.0 * random.nextDouble()).toLong()
    }
}
