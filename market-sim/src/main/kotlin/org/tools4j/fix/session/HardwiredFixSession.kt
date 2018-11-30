package org.tools4j.fix.session

import mu.KLogging
import org.tools4j.fix.RegistryFixDecoder
import org.tools4j.fix.session.FixSession.Companion.DELIMITER
import org.tools4j.messaging.PubSubMsgDispatcher
import org.tools4j.model.fix.messages.FixMessage
import org.tools4j.model.fix.messages.Message

/**
 * User: ben
 * Date: 31/8/17
 * Time: 5:06 PM
 */
class HardwiredFixSession (
        override val compId: String,
        override val targetCompId: String,
        private val messageHandler: PubSubMsgDispatcher,
        private val fixDecoder: RegistryFixDecoder) : AbstractFixSession(compId, targetCompId, messageHandler, fixDecoder) {

    companion object: KLogging()
    var counterparty: FixSession? = null

    init {
        messageHandler.subscribe(targetCompId, this);
    }

    override fun send(message: FixMessage){
        logger.info{"AnnotatedFix: Send [" + compId + "-> " + targetCompId + "]: " + message.toConsoleText()}
        logger.info{"RawFix:" + message.toFix()}
        counterparty!!.receive(message.toFix())
    }

    override fun receive(messageStr: String) {
        val message = fixDecoder.decode(messageStr, DELIMITER)
        println("Recv [" + targetCompId + "-> " + compId + "]: " + message.toConsoleText())
        messageHandler.handle(message)
    }

    override fun handle(msg: Message) {
        send(msg as FixMessage)
    }

    override fun toString(): String {
        return compId
    }
}
