package org.tools4j.model.state.client

import mu.KLogging
import org.tools4j.model.fix.messages.Message

/**
 * User: ben
 * Date: 19/7/17
 * Time: 5:24 PM
 */
abstract class AbstractState(val stateContext: StateContext) : ClientState {
    companion object: KLogging()

    override fun equals(other: Any?): Boolean {
        return this.javaClass == other!!.javaClass
    }

    override fun hashCode(): Int {
        return this.javaClass.hashCode()
    }

    override fun handle(msg: Message){
        logger.info { stateContext.order.senderCompId + "::" + stateContext.order.clOrdId + "::[" + this.javaClass + "] received msg::" + msg }
        super.handle(msg)
        stateContext.stateMessageListeners.forEach { it.onMessage(this, msg) }
    }
}
