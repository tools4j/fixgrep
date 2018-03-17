package org.tools4j.fix

import java.util.regex.Pattern

/**
 * User: ben
 * Date: 29/6/17
 * Time: 6:25 AM
 */
class SplitableByRegexString(private val str: String?, private val regexDelim: String) : SplitableString {

    override fun split(): SplitString {
        if (str == null) {
            throw NullPointerException("Cannot split a null string.")
        }
        val splitValues = str.split(regexDelim.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return SplitString(splitValues)
    }

    override fun splitAtFirst(): SplitString {
        if (str == null) {
            throw NullPointerException("Cannot split a null string.")
        }
        val pattern = Pattern.compile(regexDelim)
        val matcher = pattern.matcher(str)
        val found = matcher.find()
        if (!found) {
            return SplitString(arrayOf(str))
        }
        val startOfDelimPatternInclusive = matcher.start()
        val endOfDelimPatternExclusive = matcher.end()
        return SplitString(arrayOf(str.substring(0, startOfDelimPatternInclusive), str.substring(endOfDelimPatternExclusive)))
    }
}
