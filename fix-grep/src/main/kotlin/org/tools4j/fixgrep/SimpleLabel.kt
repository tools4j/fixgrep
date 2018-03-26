package org.tools4j.fixgrep

import java.util.regex.Pattern

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:12 AM
 */
open class SimpleLabel(val criteria: LabellingCriteria, val searchRegex: String, val replaceWith: String) : Label {
    val searchRegexPattern: Pattern by lazy {
        Pattern.compile(searchRegex)
    }

    override fun labelAndReturnNewLine(messageString: MessageString): MessageString {
        return messageString.replaceAll(searchRegexPattern, replaceWith)
    }

    override fun shouldLabel(messageString: MessageString): Boolean {
        return criteria.shouldLabel(messageString)
    }
}