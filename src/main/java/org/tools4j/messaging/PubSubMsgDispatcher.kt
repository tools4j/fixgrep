package org.tools4j.messaging

import org.tools4j.model.fix.messages.Message
import org.tools4j.model.fix.messages.MessageHandler

/**
 * User: ben
 * Date: 6/02/2018
 * Time: 5:41 PM
 */
class PubSubMsgDispatcher : MessageHandler {
    val messageHandlersByTargetCompId: MutableMap<String, MessageHandler> = HashMap()

    override fun handle(msg: Message){
        val messageHandler = messageHandlersByTargetCompId[msg.targetCompId];
        if(messageHandler == null){
            throw IllegalArgumentException("Cannot find message handler for targetCompId ${msg.targetCompId}, message handlers: $messageHandlersByTargetCompId")
        } else {
            messageHandler.handle(msg)
        }
    }

    fun subscribe(targetCompId: String, msgHandler: MessageHandler){
        messageHandlersByTargetCompId.put(targetCompId, msgHandler)
    }
}