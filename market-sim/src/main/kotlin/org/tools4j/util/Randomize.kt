package org.tools4j.util

import java.util.*

/**
 * User: ben
 * Date: 23/02/2018
 * Time: 5:29 PM
 */
class Randomize{
    private val random: Random

    init {
        this.random = Random(Date().time)
    }

    fun shouldDoConsideringPossibility(possibility: Double): Boolean {
        return random.nextDouble() <= possibility
    }

    fun <T> getRandomElement(possibleElements: Array<T>): T {
        return possibleElements[(random.nextDouble() * possibleElements.size).toInt()]
    }

    fun <T> getRandomElement(possibleElements: List<T>): T {
        return possibleElements.get((random.nextDouble() * possibleElements.size).toInt())
    }

    fun randomDouble(): Double {
        return random.nextDouble()
    }

    fun randomSign(): Int {
        return if(random.nextBoolean()) 1 else -1
    }

    fun nextInt(): Int {
        return random.nextInt()
    }

    fun nextIntBetweenZeroAndBoundExclusive(bound: Int): Int {
        return random.nextInt(bound)
    }
}