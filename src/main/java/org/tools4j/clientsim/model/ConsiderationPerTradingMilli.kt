package org.tools4j.clientsim.model

import org.tools4j.model.DateTimeService

import java.util.Date
import java.util.Random

/**
 * User: ben
 * Date: 14/7/17
 * Time: 5:27 PM
 */
class ConsiderationPerTradingMilli(private val dateTimeService: DateTimeService, private val possibilityOfOccuringPerTradingMilli: Double) : Consideration {
    private val random = Random(Date().time)
    private var timeOfLastRequest: Long = -1

    init {
        this.timeOfLastRequest = dateTimeService.now()
    }

    override fun shouldDo(): Boolean {
        val now = dateTimeService.now()
        val millisSinceLastRequest = now - timeOfLastRequest
        val possibilitySinceLastRequest = millisSinceLastRequest * possibilityOfOccuringPerTradingMilli
        val randomNumber = random.nextDouble()
        val possibility = randomNumber <= possibilitySinceLastRequest
        if (possibility) {
            println(randomNumber.toString() + " vs " + possibilitySinceLastRequest + ":" + millisSinceLastRequest)
        }
        timeOfLastRequest = now
        return possibility
    }
}
