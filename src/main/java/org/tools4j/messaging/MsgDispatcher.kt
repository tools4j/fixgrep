package org.tools4j.messaging

import org.tools4j.model.fix.messages.Message

/**
 * User: ben
 * Date: 6/02/2018
 * Time: 6:09 PM
 */
interface MsgDispatcher {
    fun dispatch(msg: Message)
}