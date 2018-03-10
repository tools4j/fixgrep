package org.tools4j.fix.session

import mu.KLogging
import org.tools4j.fix.RegistryFixDecoder
import org.tools4j.model.fix.messages.FixMessage
import org.tools4j.model.fix.messages.Message

/**
 * User: ben
 * Date: 31/8/17
 * Time: 5:06 PM
 */
class LoggingFixSession(override val compId: String, override val targetCompId: String, val fixDecoder: RegistryFixDecoder) : FixSession {
    companion object: KLogging()

    override fun receive(messageStr: String) {
        val message = fixDecoder.decode(messageStr, FixSession.DELIMITER)
        val fix = message.toAnnotatedFix()
        logger.info { "Received message $targetCompId->$compId: $fix" }
    }

    override fun send(message: FixMessage) {
        val fix = message.toAnnotatedFix()
        logger.info { "Send called on this session $compId->$targetCompId: $fix" }
    }
}
