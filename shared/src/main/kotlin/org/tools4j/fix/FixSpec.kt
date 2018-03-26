package org.tools4j.fix

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

    fun getField(tagInt: Int, valueStr: String): Field {
        val tag = getTag(tagInt)
        val value = getValue(tag, valueStr)
        return Field(tag, value)
    }

    fun getMsgTypeNameGivenCode(msgTypeCode: String): String? {
        return messageTypeCodesToNames[msgTypeCode];
    }

    private fun getTag(tag: Int): Tag {
        val tagDescription: String? = fieldsAndEnumValues[""+tag]
        return if (tagDescription != null) {
            SpecTag(tag, tagDescription)
        } else {
            UnknownTag(tag)
        }
    }

    private fun getValue(tag: Tag, rawValue: String): Value {
        val tagDescription = this.fieldsAndEnumValues["" + tag.tag + "." + rawValue]
        return if (tagDescription != null) {
            EnumValue(rawValue, tagDescription)
        } else {
            NonEnumValue(rawValue)
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
