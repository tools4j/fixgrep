package org.tools4j.fixgrep

import org.tools4j.fix.Fields
import java.util.regex.Pattern

/**
 * User: ben
 * Date: 19/03/2018
 * Time: 7:00 AM
 */
interface MessageString{
    val originalFields: Fields
    val messageText: String
    fun withMessageText(messageText: String): MessageString

    fun replaceAll(searchRegexPattern: Pattern, replaceWith: String): MessageString {
        return withMessageText(searchRegexPattern.matcher(messageText).replaceAll(replaceWith))
    }
}