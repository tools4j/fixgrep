package org.tools4j.utils

import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * User: ben
 * Date: 7/06/2017
 * Time: 6:27 AM
 */
object FormatUtils {
    private val UTC_TIMESTAMP_FORMATTER = SimpleDateFormat("yyyyMMdd-HH:mm:ss.SSS")

    fun toUTCTimestamp(time: Long): String {
        return UTC_TIMESTAMP_FORMATTER.format(time)
    }

    fun fromUTCTimestamp(time: String): Long {
        try {
            return UTC_TIMESTAMP_FORMATTER.parse(time).time
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }

    }
}
