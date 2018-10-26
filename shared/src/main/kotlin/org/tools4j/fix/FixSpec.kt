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

    fun getMsgTypeNameGivenCode(msgTypeCode: String): String? {
        return messageTypeCodesToNames[msgTypeCode];
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
            return "Exec." + ExecType.forCode(fields.getField(150)!!.value.valueRaw).name
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
