package org.tools4j.fixgrep

import java.util.regex.Pattern

/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:44 PM
 */
class RegexPrefixFixStringFromLineExtractor(extractionRegex: String): FixStringFromLineExtractor{
    val pattern: Pattern by lazy {
        Pattern.compile(extractionRegex)
    }

    override fun createLine(originalLogLine: String): String {
        val matcher = pattern.matcher(originalLogLine)
        if(!matcher.find()){
            throw IllegalArgumentException("Cannot find pattern: $pattern in line $originalLogLine")
        }
        return matcher.group(1)
    }
}