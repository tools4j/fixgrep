package org.tools4j.model.state

import org.tools4j.fix.OrdStatus
import org.tools4j.model.fix.messages.DoubleDispatchingMessageHandler
import org.tools4j.model.fix.messages.Message

/**
 * User: ben
 * Date: 23/8/17
 * Time: 6:47 AM
 */
interface State: DoubleDispatchingMessageHandler {
    fun isTerminal(): Boolean = false

    val ordStatus: OrdStatus

    val pending: Boolean
        get() = false

    val open: Boolean
        get() = true

    open fun onEntry() {}

    override
    fun throwUnsupportedMessageException(msg: Message) {
        throw UnsupportedOperationException("Message of type: " + msg.javaClass.simpleName + " not supported by state:" + this.javaClass.simpleName)
    }
}
