package org.tools4j.fix.session

import org.tools4j.fix.RegistryFixDecoder
import org.tools4j.fix.session.FixSession.Companion.DELIMITER
import org.tools4j.model.fix.messages.FixMessage
import org.tools4j.model.fix.messages.Message
import org.tools4j.model.fix.messages.MessageHandler

/**
 * User: ben
 * Date: 31/8/17
 * Time: 5:06 PM
 */
abstract class AbstractFixSession(
        open override val compId: String,
        open override val targetCompId: String,
        private val messageHandler: MessageHandler,
        private val fixDecoder: RegistryFixDecoder) : FixSession, MessageHandler {

    override fun receive(messageStr: String) {
        val message = fixDecoder.decode(messageStr, DELIMITER)
        println("Received [" + targetCompId + "-> " + compId + "]: " + message.toConsoleText())
        messageHandler.handle(message)
    }

    override fun handle(msg: Message) {
        send(msg as FixMessage)
    }

    override fun toString(): String {
        return compId
    }
}
