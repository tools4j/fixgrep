package org.tools4j.utils

import ch.qos.logback.classic.Level

/**
 * User: benjw
 * Date: 07/11/2018
 * Time: 06:25
 */
class Logging {
    companion object {
        @JvmStatic
        fun setLoggingLevel(level: Level) {
            val root = org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger
            root.level = level
        }
    }
}