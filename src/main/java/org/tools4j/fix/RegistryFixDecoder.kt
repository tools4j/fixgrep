package org.tools4j.fix

import org.tools4j.model.fix.messages.DecoderRegistry
import org.tools4j.model.fix.messages.FixMessage
import org.tools4j.model.fix.messages.Message

/**
 * User: ben
 * Date: 4/9/17
 * Time: 6:23 AM
 */
class RegistryFixDecoder(private val decoderRegistry: DecoderRegistry, private val fixSpec: FixSpec) : FixDecoder {
    override fun decode(messageStr: String, delimiter: Char): FixMessage {
        val fields = FieldsFromDelimitedString(messageStr, delimiter)
        val fieldMap = FieldMap(fields).mapValue
        val msgType = FixFieldTypes.MsgType
        val msgTypeEnum = fieldMap[msgType]!!.value.rawValue
        val msgDescription = fixSpec.getMsgType(msgTypeEnum)
        val messageFactory = decoderRegistry.get(msgTypeEnum) ?: throw IllegalArgumentException("Cannot find decoder for MsgType")
        return messageFactory.createMessage(fields)
    }
}
