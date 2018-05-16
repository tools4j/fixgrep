package org.tools4j.fix.session

import org.tools4j.model.fix.messages.FixMessage
import org.tools4j.model.fix.messages.Message

/**
 * User: ben
 * Date: 31/8/17
 * Time: 5:06 PM
 */
interface FixSession{
    val compId: String
    val targetCompId: String

    fun send(message: FixMessage)
    fun receive(messageStr: String)

    companion object {
        val DELIMITER = "\u0001"
    }
}
