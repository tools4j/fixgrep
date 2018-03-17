package org.tools4j.fixgrep

import org.tools4j.fix.Fields
import java.util.regex.Pattern

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:12 AM
 */
class SimpleLabel(val criteria: LabellingCriteria, val searchRegex: String, val replaceWith: String) : Label {
    val searchRegexPattern: Pattern by lazy {
        Pattern.compile(searchRegex)
    }

    override fun labelAndReturnNewLine(originalFields: Fields, line: String): String {
        return searchRegexPattern.matcher(line).replaceAll(replaceWith)
    }

    override fun shouldLabel(originalFields: Fields, line: String): Boolean {
        return criteria.shouldLabel(originalFields, line)
    }
}