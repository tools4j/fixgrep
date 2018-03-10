package org.tools4j.model.fix.messages

import org.tools4j.fix.Ascii1Char
import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 31/8/17
 * Time: 5:11 PM
 */
interface FixMessageDecoder<out T : FixMessage> {
    val msgType: String
    fun createMessage(str: String, delimiter: Char = Ascii1Char().toChar()): T
    fun createMessage(fields: Fields): T
}
