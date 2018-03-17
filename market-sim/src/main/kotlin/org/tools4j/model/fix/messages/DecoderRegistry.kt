package org.tools4j.model.fix.messages

import sun.plugin.dom.exception.InvalidStateException

import java.util.HashMap

/**
 * User: ben
 * Date: 31/8/17
 * Time: 5:45 PM
 */
class DecoderRegistry {
    private val messageFactoryMap: MutableMap<String, FixMessageDecoder<FixMessage>>

    init {
        messageFactoryMap = HashMap()
    }

    fun register(factory: FixMessageDecoder<FixMessage>) {
        messageFactoryMap[factory.msgType] = factory
    }

    operator fun get(messageType: String): FixMessageDecoder<FixMessage>? {
        return messageFactoryMap[messageType] ?: throw InvalidStateException("Cannot find messageFactory for messageType: " + messageType + " registered message types: " + messageFactoryMap.keys)
    }
}
