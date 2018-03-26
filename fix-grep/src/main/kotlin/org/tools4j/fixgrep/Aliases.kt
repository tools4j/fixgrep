package org.tools4j.fixgrep

import org.tools4j.fix.Fields
import java.util.function.Consumer

/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:27 PM
 */
interface FieldsProcessor: Consumer<Fields>
interface MessageStringProcessor: Consumer<MessageString>{
    class AssertLastReceivedProcessor: MessageStringProcessor {
        var lastReceived: MessageString? = null

        override fun accept(t: MessageString) {
            println(t)
            lastReceived = t
        }
    }
}

