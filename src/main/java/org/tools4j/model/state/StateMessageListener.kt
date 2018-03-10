package org.tools4j.model.state

import org.tools4j.model.fix.messages.Message

/**
 * User: ben
 * Date: 24/01/2018
 * Time: 6:42 AM
 */
interface StateMessageListener {
    fun onMessage(state: State, message: Message){
        //no-op
    }
}
