package org.tools4j.fixgrep

import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 19/03/2018
 * Time: 7:06 AM
 */
class MultiLineMessageString(override val originalFields: Fields, override val messageText: String) : MessageString {
    override fun withMessageText(messageText: String): MessageString {
        return MultiLineMessageString(originalFields, messageText)
    }

    override fun toString(): String {
        return messageText
    }
}