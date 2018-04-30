package org.tools4j.fix

import mu.KLogging

/**
 * User: ben
 * Date: 12/06/2017
 * Time: 6:43 AM
 */
class FixSpec(
        val fieldsAndEnumValues: Map<String, String>,
        val headerFields: Set<String>,
        val trailerFields: Set<String>,
        val messageTypeCodesToNames: Map<String, String>) {

    companion object: KLogging()

    fun getField(tagInt: Int, valueStr: String): Field {
        val tag = getTag(tagInt)
        val value = getValue(tag, valueStr)
        return FieldImpl(tag, value)
    }

    fun getMsgTypeNameGivenCode(msgTypeCode: String): String? {
        return messageTypeCodesToNames[msgTypeCode];
    }

    private fun getTag(tag: Int): Tag {
        val tagDescription: String? = fieldsAndEnumValues[""+tag]
        return if (tagDescription != null) {
            AnnotatedTag(tag, tagDescription)
        } else {
            UnknownTag(tag)
        }
    }

    private fun getValue(tag: Tag, rawValue: String): Value {
        val tagDescription = this.fieldsAndEnumValues["" + tag.tag + "." + rawValue]
        return if (tagDescription != null) {
            AnnotatedValue(rawValue, tagDescription)
        } else {
            NonEnumValue(rawValue)
        }
    }

    fun msgTypeAndExecTypeName(fields: Fields): String {
        if(fields.msgTypeCode != "8"){
            val msgTypeName = messageTypeCodesToNames.get(fields.msgTypeCode)
            if(msgTypeName == null) {
                logger.debug { "Could not find message type for msgTypeCode: $fields.msgTypeCode returning 'Unknown'" }
                return "Unknown"
            } else {
                return msgTypeName
            }
        } else if(fields.getField(150) != null){
            return "Exec." + ExecType.forCode(fields.getField(150)!!.value.rawValue).name
        } else {
            return "ExecutionReport"
        }
    }

    override fun toString(): String {
        return "FixspecProperties{" + "\n" +
                "    fieldsAndEnumValues=" + fieldsAndEnumValues + "\n" +
                "    headerFields=" + headerFields + "\n" +
                "    trailerFields=" + trailerFields + "\n" +
                "    messageTypes=" + messageTypeCodesToNames + "\n" +
                '}'.toString()
    }
}
