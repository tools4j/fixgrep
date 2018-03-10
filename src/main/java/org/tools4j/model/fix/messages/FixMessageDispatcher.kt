package org.tools4j.model.fix.messages

/**
 * User: ben
 * Date: 7/9/17
 * Time: 6:27 AM
 */
interface FixMessageDispatcher {
    fun dispatch(message: Message)
}
