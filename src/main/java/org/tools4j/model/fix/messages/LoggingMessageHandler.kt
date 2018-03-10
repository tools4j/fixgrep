package org.tools4j.model.fix.messages

import mu.KLogging

/**
 * User: ben
 * Date: 7/9/17
 * Time: 6:36 AM
 */
class LoggingMessageHandler: MessageHandler {
    companion object: KLogging()

    override fun handle(msg: Message){
        logger.info { "Received message: $msg" }
    }
}
