package org.tools4j.fix

import org.tools4j.properties.PropertyKeysAndDefaultValues

/**
 * User: ben
 * Date: 12/06/2017
 * Time: 6:42 AM
 */
class FixgrepCommandLine(private val commandLine: String, private val propertyKeysAndDefaultValues: PropertyKeysAndDefaultValues) {

    fun parse(): String {
        return commandLine
    }
}
