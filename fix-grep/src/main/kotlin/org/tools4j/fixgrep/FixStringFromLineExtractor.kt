package org.tools4j.fixgrep

/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:43 PM
 */
interface FixStringFromLineExtractor {
    fun createLine(originalLogLine: String): String;
}