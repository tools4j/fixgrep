package org.tools4j.fix

import org.tools4j.model.fix.messages.DecoderRegistry
import org.tools4j.model.fix.messages.FixMessage

/**
 * User: ben
 * Date: 4/9/17
 * Time: 6:23 AM
 */
class RegistryFixDecoder(private val decoderRegistry: DecoderRegistry, private val fixSpec: FixSpec) : FixDecoder {
    override fun decode(messageStr: String, delimiter: Char): FixMessage {
        val fields = FieldsFromDelimitedString(messageStr, delimiter).fields
        val msgTypeCode  = fields.getField(FixFieldTypes.MsgType)!!.value.rawValue
        val msgDescription = fixSpec.getMsgTypeNameGivenCode(msgTypeCode)
        val messageFactory = decoderRegistry.get(msgTypeCode) ?: throw IllegalArgumentException("Cannot find decoder for MsgType")
        return messageFactory.createMessage(fields)
    }
}
