package org.tools4j.model

import java.util.*

/**
 * User: ben
 * Date: 7/06/2017
 * Time: 7:01 PM
 */
class DateTimeService {
    var dateOverride: Long? = null

    fun now(): Long {
        return dateOverride ?: Date().time
    }
}
